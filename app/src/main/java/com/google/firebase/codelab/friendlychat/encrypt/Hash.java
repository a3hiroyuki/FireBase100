/**
 * ハッシュ値計算
 */
package com.google.firebase.codelab.friendlychat.encrypt;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;


/**
 * @author M.Hashimoto
 *
 */
public class Hash {
	private static final String TAG = "Hash";
	private static final int BUFFER_LENGTH = 512;

	/**
	 * アルゴリズム列挙型
	 * @author M.Hashimoto
	 *
	 */
	public enum Algorithm {
		/**
		 * SHA
		 */
		SHA("SHA"),

		/**
		 * SHA-256
		 */
		SHA256("SHA-256"),

		/**
		 * SHA-386
		 */
		SHA384("SHA-386"),

		/**
		 * SHA-512
		 */
		SHA512("SHA-512"),

		/**
		 * MD5
		 */
		MD5("MD5");
		private String name;
		private Algorithm(String name) {
			this.name = name;
		}
		/**
		 * @return アルゴリズム名
		 */
		public String getName() {
			return this.name;
		}
	}

	private Algorithm algo;
	private MessageDigest messageDigest;

	/**
	 * コンストラクタ
	 * @param algo
	 * @throws NoSuchAlgorithmException
	 */
	public Hash(final Algorithm algo) throws NoSuchAlgorithmException {
		this.algo = algo;
		try {
			this.messageDigest = MessageDigest.getInstance(algo.getName());

		} catch (NoSuchAlgorithmException exn) {
			Log.w(TAG, exn.getMessage());
			throw exn;
		}
	}

	/**
	 * デフォルトコンストラクタ
	 * @throws Exception
	 */
	public Hash() throws NoSuchAlgorithmException {
		this(Algorithm.SHA256);
	}

	/**
	 * アルゴリズムを返す
	 * @return アルゴリズム
	 */
	public Algorithm getAlgorithm() {
		return this.algo;
	}

	/**
	 * 文字列のハッシュ値を計算する
	 * <p>
	 * 文字コードはUTF-8
	 * </p>
	 * @param message
	 * @return ハッシュ値
	 * @throws UnsupportedEncodingException
	 */
	public byte[] valueOf(final String message)
		throws UnsupportedEncodingException {

		return this.valueOf(message, "UTF-8");
	}

	/**
	 * 文字列のハッシュ値を計算する
	 * @param message
	 * @param charsetName
	 * @return ハッシュ値
	 * @throws UnsupportedEncodingException
	 */
	public byte[] valueOf(final String message, final String charsetName)
			throws UnsupportedEncodingException {

		return this.valueOf(message.getBytes(charsetName));
	}

	/**
	 * 文字列のハッシュ値をソルトとストレッチング付きで計算する
	 * @param message ハッシュ計算の対象となる文字列
	 * @param charsetName 文字コード
	 * @param salt ソルト(nullまたはサイズ0の場合はソルト無し)
	 * @param stretching ストレッチングの回数。但し1以下が指定された場合は1回(ストレッチング無し)
	 * @return ハッシュ値
	 * @throws UnsupportedEncodingException 
	 */
	public byte[] valueOf(final String message, final String charsetName,
												final byte[] salt, final int stretching)
			throws UnsupportedEncodingException {

		return this.valueOf(message.getBytes(charsetName), salt, stretching);
	}

	/**
	 * バイト列のハッシュ値を計算する
	 * @param data
	 * @return ハッシュ値
	 */
	public byte[] valueOf(final byte[] data) {
		return this.valueOf(data, null, 1);
	}

	/**
	 * バイト列のハッシュ値をソルトとストレッチング付きで計算する
	 * @param data ハッシュ計算の対象となるバイト列
	 * @param salt ソルト(nullまたはサイズ0の場合はソルト無し)
	 * @param stretching ストレッチングの回数。但し1以下が指定された場合は1回(ストレッチング無し)
	 * @return ハッシュ値
	 */
	public byte[] valueOf(final byte[] data, final byte[] salt,
												final int stretching) {
		byte[] saltData = (salt != null) ? salt : new byte[0];
		int count = (stretching > 1) ? stretching : 1;

		byte[] hash = new byte[0];
		synchronized (this.messageDigest) {
			for (int i = 0; i < count; i++) {
				this.messageDigest.update(hash);
				this.messageDigest.update(data);
				this.messageDigest.update(saltData);
				hash = this.messageDigest.digest();
			}
		}
		return hash;
	}

	/**
	 * ファイルのハッシュ値を計算する
	 * @param file
	 * @return ハッシュ値
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public byte[] valueOf(final File file)
		throws FileNotFoundException, IOException {

		try {
			return this.valueOf(new FileInputStream(file));
		} catch (FileNotFoundException exn) {
			Log.w(TAG, exn.getMessage());
			throw exn;
		}
	}

	/**
	 * 入力ストリームのハッシュ値を計算する
	 * @param is
	 * @return ハッシュ値
	 * @throws IOException
	 */
	public byte[] valueOf(final InputStream is) throws IOException {
		synchronized (this.messageDigest) {
			DigestInputStream dis = new DigestInputStream(is, this.messageDigest);
			dis.on(true);

			int n = 0;
			byte[] buf = new byte[BUFFER_LENGTH];

			try {
				while (true) {
					n = dis.read(buf);
					if (n < 0)
						break;
				}
				return this.messageDigest.digest();

			} catch (IOException exn) {
				Log.w(TAG, exn.getMessage());
				throw exn;
			} finally {
				SecurityUtils.closeSilently(dis);
				SecurityUtils.closeSilently(is);
			}
		}
	}

	/**
	 * 利用可能なメッセージダイジェストアルゴリズムを調べる
	 * @return 利用可能なメッセージダイジェストアルゴリズム名のSet
	 */
	public static Set<String> getAvailableAlgorithms() {
		return SecurityUtils.getAvailableAlgorithms("MessageDigest");
	}
}
