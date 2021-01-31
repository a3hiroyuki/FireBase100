/**
 * 暗号化に必要な機能（抽象クラス）
 */
package com.google.firebase.codelab.friendlychat.encrypt;

import android.util.Log;

import com.google.firebase.codelab.friendlychat.PRNGAlgorithm;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 * @author M.Hashimoto
 *
 */
public abstract class AbstractCipher {
	private static final String TAG = "AbstractCipher";

	private PRNGAlgorithm prng;
	private CipherMode mode;
	private Padding padding;
	private int keySize;

	public static PRNGAlgorithm DEFAULT_PRNG = PRNGAlgorithm.SHA1PRNG;

	/**
	 * コンストラクタ
	 * @param mode
	 * @param padding
	 * @param keySize 鍵のビット数
	 */
	protected AbstractCipher(final CipherMode mode, final Padding padding,
													 final int keySize) {
		this.prng = DEFAULT_PRNG;
		this.mode = mode;
		this.padding = padding;
		this.keySize = keySize;
	}

	protected abstract Cipher getCipher();

	/**
	 * @return 暗号利用モード
	 */
	public CipherMode getCipherMode() {
		return this.mode;
	}

	/**
	 * @return パディング
	 */
	public Padding getPadding() {
		return this.padding;
	}

	/**
	 * @return 疑似乱数生成アルゴリズム
	 */
	public PRNGAlgorithm getPRNGAlgorithm() {
		return this.prng;
	}

	/**
	 * @return 鍵のビット数
	 */
	public int getKeySize() {
		return this.keySize;
	}

	/**
	 * バイト列を暗号化
	 * <p>
	 * initial vectorは使用しない（利用モードはECBを仮定）
	 * </p>
	 * @param data
	 * @param key
	 * @return 暗号化されたバイト列
	 * @throws Exception
	 */
	public byte[] encrypt(final byte[] data, final Key key)
		throws InvalidKeyException, NoSuchAlgorithmException,
					 IllegalBlockSizeException, BadPaddingException {

		final String tag = TAG+".encrypt";

		Cipher cipher = this.getCipher();
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key,
									SecureRandom.getInstance(this.getPRNGAlgorithm().toString()));
			return cipher.doFinal(data);

		} catch (InvalidKeyException exn) {
			Log.w(tag, exn.getMessage());
			throw exn;

		} catch (NoSuchAlgorithmException exn) {
			Log.w(tag, exn.getMessage());
			throw exn;

		} catch (IllegalBlockSizeException exn) {
			Log.w(tag, exn.getMessage());
			throw exn;

		} catch (BadPaddingException exn) {
			Log.w(tag, exn.getMessage());
			throw exn;
		}
	}

	/**
	 * バイト列を復号
	 * <p>
	 * initial vectorは使用しない（利用モードはECBを仮定）
	 * </p>
	 * @param data
	 * @param key
	 * @return 復号されたバイト列
	 * @throws Exception
	 */
	public byte[] decrypt(final byte[] data, final Key key)
		throws InvalidKeyException, NoSuchAlgorithmException,
					 IllegalBlockSizeException, BadPaddingException {

		final String tag = TAG+".decrypt";

		Cipher cipher = this.getCipher();
		try {
			cipher.init(Cipher.DECRYPT_MODE, key,
									SecureRandom.getInstance(this.getPRNGAlgorithm().toString()));
			return cipher.doFinal(data);

		} catch (InvalidKeyException exn) {
			Log.w(tag, exn.getMessage());
			throw exn;

		} catch (NoSuchAlgorithmException exn) {
			Log.w(tag, exn.getMessage());
			throw exn;

		} catch (IllegalBlockSizeException exn) {
			Log.w(tag, exn.getMessage());
			throw exn;

		} catch (BadPaddingException exn) {
			Log.w(tag, exn.getMessage());
			throw exn;
		}
	}
}
