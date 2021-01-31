/**
 * ブロック暗号利用モード
 * <p>
 * 詳しくは、<a href="http://csrc.nist.gov/publications/nistpubs/800-38a/sp800-38a.pdf">NISTのドキュメント</a>
 * 等参照のこと。
 * </p>
 */

package com.google.firebase.codelab.friendlychat.encrypt;

/**
 * @author M.Hashimoto
 *
 */
public enum CipherMode {
	/**
	 * Electronic codebook
	 */
	ECB,

	/**
	 * Cipher block chaining
	 */
	CBC,

	/**
	 * Output feedback
	 */
	OFB,

	/**
	 * Cipher feedback
	 */
	CFB,

	/**
	 * Counter
	 */
	CTR,

	/**
	 * Cipher Text Stealing
	 */
	CTS,

	/**
	 * Propagating cipher block chaining
	 */
	PCBC;
}
