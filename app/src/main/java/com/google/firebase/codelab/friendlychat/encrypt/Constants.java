package com.google.firebase.codelab.friendlychat.encrypt;

import java.nio.charset.Charset;


/**
 * PLRの各モジュールから共通に使用される定数を定義したモジュールです。
 */
public class Constants {
	/**
	 * UTF-8文字セット定数
	 */
	public static Charset CHARSET_UTF8; static {
		try {
			CHARSET_UTF8 = Charset.forName("UTF-8");
		} catch (Exception e) {
			// IllegalCharsetNameException, IllegalArgumentException,
			// UnsupportedCharsetException

			// should not occur.
			e.printStackTrace();
			CHARSET_UTF8 = null;
		}
	}

	/**
	 * PLRルートフォルダ名
	 */
	public static final String PLR_ROOT_FOLDER_NAME = "PLR";

	/**
	 * PLRルートフォルダ
	 */
	public static final String PLR_ROOT_FOLDER = PLR_ROOT_FOLDER_NAME + "/";

	/**
	 * PLRバージョンファイル名
	 */
	public static final String PLR_VERSION_FILE_NAME = "plr_version";

	/**
	 * PLRメタ情報フォルダ名
	 */
	public static final String PLR_META_FOLDER_NAME = ".plr.meta";

	/**
	 * PLRメタ情報フォルダ
	 */
	public static final String PLR_META_FOLDER =
		PLR_ROOT_FOLDER + PLR_META_FOLDER_NAME + "/";

	/**
	 * PLRコントロールファイルプレフィックス
	 */
	public static final String PLR_CONTROL_PREFIX = ".plr.";

	/**
	 * PLRユーザメタフォルダプレフィックス
	 */
	public static final String PLR_USER_META_FOLDER_PREFIX =
		PLR_CONTROL_PREFIX + "meta.";

	/**
	 * PLRメタ共有情報
	 */
	public static final String PLR_META_SHARE_PREFIX =
		PLR_CONTROL_PREFIX + "share.";

	/**
	 * PLRメタ共有解除情報
	 */
	public static final String PLR_META_UNSHARE_PREFIX =
		PLR_CONTROL_PREFIX + "unshare.";

	/**
	 * PLRメタフレンドリクエスト情報
	 */
	public static final String PLR_META_FRIEND_REQUEST_PREFIX =
		PLR_META_SHARE_PREFIX + "friendRequest.";

	/**
	 * 鍵ファイルサフィックス
	 */
	public static final String PLR_KEY_SUFFIX = ".key";

	/**
	 * 公開鍵ファイル名
	 */
	public static final String PLR_PUBLIC_KEY_FILE_NAME =
		"public" + PLR_KEY_SUFFIX;

	/**
	 * 秘密鍵ファイル名
	 */
	public static final String PLR_PRIVATE_KEY_FILE_NAME =
		"private" + PLR_KEY_SUFFIX;

	/**
	 * PLRメタ鍵情報(フォルダ)
	 */
	public static final String PLR_META_KEY_FILE_NAME =
		PLR_CONTROL_PREFIX + "key";

	/**
	 * PLRメタ鍵情報
	 */
	public static final String PLR_META_KEY_PREFIX =
		PLR_CONTROL_PREFIX + "key.";

	/**
	 * PLRメタ開示鍵情報
	 */
	public static final String PLR_META_SHARED_KEY_PREFIX =
		PLR_CONTROL_PREFIX + "skey.";

	/**
	 * PLR鍵フォルダ名
	 */
	public static final String PLR_KEY_FOLDER_NAME = ".plr.keys";

	/**
	 * PLR鍵フォルダ
	 */
	public static final String PLR_KEY_FOLDER = PLR_KEY_FOLDER_NAME + "/";

	/**
	 * PLR証明書フォルダ名
	 */
	public static final String PLR_CERTS_FOLDER_NAME = ".plr.certs";

	/**
	 * PLR証明書フォルダ
	 */
	public static final String PLR_CERTS_FOLDER =
		PLR_ROOT_FOLDER + PLR_CERTS_FOLDER_NAME + "/";

	/**
	 * PLR証明書サフィックス
	 */
	public static final String PLR_CERT_SUFFIX = ".cert";

	/**
	 * PLRデフォルトアカウント名
	 */
	public static final String PLR_DEFAULT_ACCOUNT = "default";

	/**
	 * PLRデフォルトアカウントフォルダ
	 */
	public static final String PLR_DEFAULT_ACCOUNT_FOLDER =
		PLR_ROOT_FOLDER + PLR_DEFAULT_ACCOUNT + "/";

	/**
	 * PLR公開フォルダ名
	 */
	public static final String PLR_PUBLIC_FOLDER_NAME = "public";

	/**
	 * PLR公開フォルダ
	 */
	public static final String PLR_PUBLIC_FOLDER =
		PLR_DEFAULT_ACCOUNT_FOLDER + PLR_PUBLIC_FOLDER_NAME + "/";

	/**
	 * shared情報ファイルプレフィックス
	 */
	public static final String PLR_SHARED_FILE_PREFIX =
		PLR_CONTROL_PREFIX + "shared.";

	/**
	 * PLR公開プロフィールフォルダ名
	 */
	@Deprecated
	public static final String PLR_PROFILE_FOLDER_NAME = "profile";

	/**
	 * PLR公開プロフィールフォルダ
	 */
	public static final String PLR_PROFILE_FOLDER =
		PLR_PUBLIC_FOLDER + PLR_PROFILE_FOLDER_NAME + "/";

	/**
	 * PLR公開フレンドフォルダ名
	 */
	public static final String PLR_FRIENDS_FOLDER_NAME = "friends";

	/**
	 * PLR公開フレンドフォルダ
	 */
	public static final String PLR_FRIENDS_FOLDER =
		PLR_PUBLIC_FOLDER + PLR_FRIENDS_FOLDER_NAME + "/";

	/**
	 * PLR公開グループフォルダ名
	 */
	public static final String PLR_GROUPS_FOLDER_NAME = "groups";

	/**
	 * PLR公開グループフォルダ
	 */
	public static final String PLR_GROUPS_FOLDER =
		PLR_PUBLIC_FOLDER + PLR_GROUPS_FOLDER_NAME + "/";
}
