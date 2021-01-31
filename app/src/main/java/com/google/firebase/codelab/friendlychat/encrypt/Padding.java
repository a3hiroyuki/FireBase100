/**
 * パディング方式
 * <p>
 * 詳しくは、<a href="http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html">Oracleのドキュメント</a>
 * 等参照のこと。
 * </p>
 */
package com.google.firebase.codelab.friendlychat.encrypt;

/**
 * @author M.Hashimoto
 *
 */
public enum Padding {

	/**
	 * パディングなし
	 */
	None("NoPadding"),

	/**
	 * PKCS#1パディング
	 */
	PKCS1("PKCS1Padding"),

	/**
	 * PKCS#5パディング
	 */
	PKCS5("PKCS5Padding"),

	/**
	 * ISO10126パディング
	 */
	ISO10126("ISO10126Padding"),

	/**
	 * Optimal Asymmetric Encryption Padding
	 */
	OAEP_SHA1_MGF1("OAEPWithSHA-1AndMGF1Padding"),

	/**
	 * Optimal Asymmetric Encryption Padding
	 */
	OAEP_SHA256_MGF1("OAEPWithSHA-256AndMGF1Padding"),

	/**
	 * SSL3パディング
	 */
	SSL3("SSL3Padding");

	private String name;

	private Padding(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
