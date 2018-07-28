package com.blueocean.azbrain.util;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Crypto util class.
 *
 * Created by @author wangzunhui on 2017/6/13.
 */
public class CryptoUtil {
    private static final  Logger logger = LoggerFactory.getLogger(CryptoUtil.class);
    private static final  String AES_ALG = "AES";
    private static final  String AES_MODE = "AES/ECB/PKCS5Padding";
    private static final  int AES_KEY_LENGTH = 128;

    private CryptoUtil() {
        throw new IllegalStateException("Utility Crypto");
    }

    private static SecretKeySpec generatorKey(String key) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(CryptoUtil.AES_ALG);
            keygen.init(CryptoUtil.AES_KEY_LENGTH, new SecureRandom(key.getBytes()));
            SecretKey secretKey = keygen.generateKey();
            byte[] raw = secretKey.getEncoded();
            return new SecretKeySpec(raw, CryptoUtil.AES_ALG);
        }catch (NoSuchAlgorithmException e){
            logger.error("generator key exception: ", e);
            return null;
        }
    }

    /**
     * AES encrypt.
     * @param val raw data
     * @return encrypt data
     * @throws Exception
     */
    public static String encrypt(String val, String key){
        SecretKeySpec keySpec = generatorKey(key);
        if (keySpec == null || val == null || val.length() == 0){
            throw new IllegalArgumentException("encrypt: invalid key or value!");
        }

        try{
            Cipher cipher = Cipher.getInstance(CryptoUtil.AES_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(val.getBytes());
            return Base64.getUrlEncoder().encodeToString(encrypted);
        }catch (Exception e){
            logger.error("encrypt exception: ", e);
            return null;
        }
    }

    /**
     * AES decrypt
     * @param val encrypt data
     * @return raw data
     * @throws Exception
     */
    public static String decrypt(String val, String key){
        SecretKeySpec keySpec = generatorKey(key);
        if (keySpec == null || val == null || val.length() == 0){
            throw new IllegalArgumentException("decrypt: invalid key or value!");
        }

        try {
            Cipher cipher = Cipher.getInstance(CryptoUtil.AES_MODE);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypted = cipher.doFinal(Base64.getUrlDecoder().decode(val));
            return new String(decrypted);
        }catch (Exception e){
            logger.error("decrypt exception: ", e);
            return null;
        }
    }

    /**
     * hash256
     *
     * @param args string list
     * @return hash value
     */
    public static String signature(String... args){
        Hasher hasher = Hashing.sha256().newHasher();
        for (String arg : args){
            if (!StringUtils.isEmpty(arg)){
                hasher.putString(arg, Charset.forName("utf-8"));
            }
        }

        return hasher.hash().toString();
    }

    /**
     * sha1
     *
     * @param args string list
     * @return hash value
     */
    public static String sha1(String... args){
        Hasher hasher = Hashing.sha1().newHasher();
        for (String arg : args){
            if (!StringUtils.isEmpty(arg)){
                hasher.putString(arg, Charset.forName("utf-8"));
            }
        }

        return hasher.hash().toString();
    }

    /**
     * md5
     *
     * @param args string list
     * @return hash value
     */
    public static String md5(String... args){
        Hasher hasher = Hashing.md5().newHasher();
        for (String arg : args){
            if (!StringUtils.isEmpty(arg)){
                hasher.putString(arg, Charset.forName("utf-8"));
            }
        }

        return hasher.hash().toString();
    }
}
