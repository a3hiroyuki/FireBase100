/**
 *
 */
package com.google.firebase.codelab.friendlychat.encrypt;

import android.util.Base64;

import com.google.firebase.codelab.friendlychat.encrypt.Constants;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Set;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * @author M.Hashimoto
 *
 */
public class SecurityUtils {
	private static final String PRNG_ALGORITHM = "SHA1PRNG";
	private static final String HASH_ALGORITHM = "SHA-256";

	private static final String SECRET_KEY_SEP = ":";

	private static final int BUF_SIZE = 1024;

	private static MessageDigest MESSAGE_DIGEST; static {
		try {
			MESSAGE_DIGEST = MessageDigest.getInstance(HASH_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			// should not occur.
			e.printStackTrace();
			MESSAGE_DIGEST = null;
		}
	}

	/**
	 * 文字列から、そのハッシュ値を求めます。
	 *
	 * @return ハッシュ値の文字列表現
	 */
	public static String generateHash(String id) {
		synchronized (MESSAGE_DIGEST) {
			MESSAGE_DIGEST.update(id.getBytes(Constants.CHARSET_UTF8));
			return byteArrayToString(MESSAGE_DIGEST.digest());
		}
	}

//	/**
//	 * 共通鍵を生成します。
//	 *
//	 * @return 共通鍵
//	 */
//	public static SecretKey generateSecretKey() {
//		try {
//			return new SymmetricCipher().generateKey();
//		} catch (Exception e) {
//			// NoSuchAlgorithmException?
//			return null;
//		}
//	}

	/**
	 * 鍵を文字列に変換します。
	 *
	 * @param secretKey 鍵
	 * @return 鍵の文字列表現
	 */
	public static String secretKeyToString(SecretKey secretKey) {
		return secretKey.getAlgorithm() + SECRET_KEY_SEP +
			Base64.encodeToString(secretKey.getEncoded(),
														Base64.NO_WRAP|Base64.NO_PADDING);
	}

	/**
	 * 文字列を鍵に変換します。
	 *
	 * @param secretKeyStr 鍵の文字列表現
	 * @return 鍵
	 * @exception IllegalArgumentException 鍵の文字列表現が不正な場合
	 */
	public static SecretKey stringToSecretKey(String secretKeyStr)
		throws IllegalArgumentException {

		String algorithm, encodedStr; {
			String[] a = secretKeyStr.split(SECRET_KEY_SEP, 2);
			if (a.length < 2)
				throw new IllegalArgumentException(
					"Key string has no algorithm section.");

			algorithm = a[0];
			encodedStr = a[1];
		}
		return new SecretKeySpec(Base64.decode(encodedStr, Base64.DEFAULT),
														 algorithm);
	}

	/**
	 * 利用可能なアルゴリズムを調べる
	 * @param serviceName
	 * @return アルゴリズム名のSet
	 */
	public static Set<String> getAvailableAlgorithms(String serviceName) {
		Set<String> s = Security.getAlgorithms(serviceName);
		return s;
	}

	/**
	 * 利用可能な暗号アルゴリズムを調べる
	 * @return 利用可能な暗号アルゴリズム名のSet
	 */
	public static Set<String> getAvailableCipherAlgorithms() {
		return getAvailableAlgorithms("Cipher");
	}


	/**
	 * バイト列を文字列に変換
	 * @param ba
	 * @return 文字列
	 */
	public static String byteArrayToString(byte[] ba) {
		StringBuilder sb = new StringBuilder();
		for (int b : ba) {
			sb.append(Character.forDigit(b >> 4 & 0xf, 16));
			sb.append(Character.forDigit(b & 0xf, 16));
		}
		return sb.toString();
	}

	/**
	 * バイト列の文字列表現をバイト列に変換
	 *
	 * @param byteString バイト列の文字列表現
	 * @return バイト列
	 */
	public static byte[] stringToByteArray(String byteString) {
		int len = byteString.length();
		byte[] bytes = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			bytes[i / 2] =
				(byte)((Character.digit(byteString.charAt(i), 16) << 4 |
								Character.digit(byteString.charAt(i + 1), 16)) & 0xff);
		}
		return bytes;
	}

	/**
	 * Keyの文字列表現を得る
	 * @param key
	 * @return 文字列表現
	 */
	public static String keyToString(Key key) {
		return key.getAlgorithm()+":"+byteArrayToString(key.getEncoded())+"("+key.getFormat()+")";
	}

	/**
	 * Closeableを閉じる
	 * @param closeable
	 */
	public static void closeSilently(final Closeable closeable) {
		if (closeable != null)
			try {
				closeable.close();
			} catch (IOException e) {
				//
			}
	}

	/**
	 * バイト配列をゼロで埋める
	 * @param ba
	 */
	public static void zeroOut(final byte[] ba) {
		Arrays.fill(ba, (byte)0x00);
	}

	/**
	 * 文字配列を空白で埋める
	 * @param ca
	 */
	public static void zeroOut(final char[] ca) {
		Arrays.fill(ca, ' ');
	}

	/**
	 * ランダムなバイト列を生成する
	 * @param size
	 * @return バイト列
	 * @throws Exception
	 */
	public static byte[] generateByteArray(final int size) throws Exception {
		byte[] ba = new byte[size];
		try {
			SecureRandom sr = SecureRandom.getInstance(PRNG_ALGORITHM);
			sr.nextBytes(ba);

		} catch (NoSuchAlgorithmException exn) {
			//Log.w("SecurityUtils.generateByteArray", exn.getMessage());
			throw exn;
		}
		return ba;
	}

	public static byte[] streamToByteArray(InputStream is)
		throws IOException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();

    try {
			int len;
			byte[] buf = new byte[BUF_SIZE];
			while ((len = is.read(buf)) >= 0)
				os.write(buf, 0, len);
    } finally {
			try {
				is.close();
			} catch (IOException e) {}
		}
		return os.toByteArray();
	}

	private SecurityUtils() {}
}
