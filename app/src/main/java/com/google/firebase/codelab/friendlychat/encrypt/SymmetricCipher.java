package com.google.firebase.codelab.friendlychat.encrypt;

import android.util.Log;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by 4925011 on 2017/02/22.
 */

public class SymmetricCipher extends AbstractCipher{

    /**
     * 標準の暗号アルゴリズム
     */
    public static final Algorithm DEFAULT_ALGORITHM = Algorithm.AES;
    /**
     * 標準のブロック暗号利用モード
     */
    public static final CipherMode DEFAULT_CIPHER_MODE = CipherMode.CBC;
    /**
     * 標準のパディング方式
     */
    public static final Padding DEFAULT_PADDING = Padding.PKCS5;
    /**
     * 標準の鍵サイズ
     */
    public static final int DEFAULT_KEY_SIZE = 128;

    /**
     * デフォルトコンストラクタ
     * @throws Exception
     */
    public SymmetricCipher() throws Exception {
        this(DEFAULT_ALGORITHM, DEFAULT_CIPHER_MODE, DEFAULT_PADDING, DEFAULT_KEY_SIZE);
    }

    /**
     * コンストラクタ
     * @param algo
     * @param mode
     * @param padding
     * @param keySize
     * @throws Exception
     */
    public SymmetricCipher(final Algorithm algo, final CipherMode mode,
                           final Padding padding, final int keySize)
            throws NoSuchAlgorithmException, NoSuchPaddingException {

        super(mode, padding, keySize);
        this.algorithm = algo;

        try {
            this.cipher = Cipher.getInstance(algo+"/"+mode+"/"+padding);

        } catch (NoSuchAlgorithmException exn) {
            Log.w(TAG, exn.getMessage());
            throw exn;

        } catch (NoSuchPaddingException exn) {
            Log.w(TAG, exn.getMessage());
            throw exn;
        }
    }

    @Override
    protected Cipher getCipher() {
        return null;
    }

    public enum Algorithm {
        AES,
        BROWFISH,
        DES;
    }

    private static final String TAG = "SymmetricCipher";

    private Algorithm algorithm;

    private Cipher cipher;


    public byte[] encrypt(final byte[] data, final SecretKey key)
            throws InvalidKeyException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException {

        try {
            return SecurityUtils.streamToByteArray(
                    this.encrypt(new ByteArrayInputStream(data), key));

        } catch (IOException e) {
            // should not occur.
            e.printStackTrace();
            return null;
        }
    }

    /**
     * バイト列を復号
     *
     * @param data
     * @param key
     * @return 復号されたバイト列
     * @throws Exception
     */
    public byte[] decrypt(final byte[] data, final SecretKey key)
            throws InvalidKeyException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException, IOException {

        return SecurityUtils.streamToByteArray(
                this.decrypt(new ByteArrayInputStream(data), key));
    }


    /**
     * 入力ストリームを共通鍵で暗号化
     * initial vectorに続いて暗号化されたコンテンツが配置される
     * @param is 入力ストリーム
     * @param key 共通鍵
     * @return 暗号化された入力ストリーム
     * @throws Exception
     */
    public InputStream encrypt(InputStream is, SecretKey key)
            throws InvalidKeyException, NoSuchAlgorithmException {

        final String tag = TAG+".encrypt";

        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] iv = cipher.getIV();
            if (iv == null)
                iv = this.generateByteArray(this.cipher.getBlockSize());

            return new SequenceInputStream(new ByteArrayInputStream(iv),
                    new CipherInputStream(is, cipher));

        } catch (InvalidKeyException exn) {
            Log.w(tag, exn.getMessage());
            throw exn;
        }
    }

    /**
     * ランダムなバイト列を生成する
     * @param size
     * @return バイト列
     * @throws Exception
     */
    public byte[] generateByteArray(final int size)
            throws NoSuchAlgorithmException {

        byte[] ba = new byte[size];
        try {
            SecureRandom.getInstance(this.getPRNGAlgorithm().toString())
                    .nextBytes(ba);

        } catch (NoSuchAlgorithmException exn) {
            //Log.w(TAG+".generateByteArray", exn.getMessage());
            throw exn;
        }
        return ba;
    }

    /**
     * 入力ストリームを共通鍵で復号する
     * 先頭のブロックサイズバイトはinitial vectorとして扱う
     * @param is 入力ストリーム
     * @param key 共通鍵
     * @return 復号された入力ストリーム
     * @throws Exception
     */
    public InputStream decrypt(InputStream is, SecretKey key)
            throws InvalidKeyException, InvalidAlgorithmParameterException,
            IOException {

        final String tag = TAG+".decrypt";

        try {
            byte[] iv = new byte[this.cipher.getBlockSize()];
            is.read(iv);

            this.cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return new CipherInputStream(is, cipher);

        } catch (InvalidKeyException exn) {
            Log.w(tag, exn.getMessage());
            throw exn;

        } catch (InvalidAlgorithmParameterException exn) {
            Log.w(tag, exn.getMessage());
            throw exn;

        } catch (IOException exn) {
            Log.w(tag, exn.getMessage());
            throw exn;
        }
    }


    /**
     * 共通鍵をランダムに生成する
     * @return 共通鍵
     * @throws Exception
     */
    public SecretKey generateKey() throws NoSuchAlgorithmException {
        try {
            KeyGenerator gen = KeyGenerator.getInstance(this.algorithm.toString());
            gen.init(this.getKeySize(),
                    SecureRandom.getInstance(this.getPRNGAlgorithm().toString()));

            return gen.generateKey();

        } catch (NoSuchAlgorithmException exn) {
            //Log.w(TAG+".generateKey", exn.getMessage());
            throw exn;
        }
    }

}
