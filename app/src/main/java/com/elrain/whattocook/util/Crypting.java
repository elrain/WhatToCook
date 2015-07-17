package com.elrain.whattocook.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Created by elrain on 15.07.15.
 */
public class Crypting {

    private static final char[] PASSWORD = "zEvXFvOccr2P5AYmczQr".toCharArray();
    private static final byte[] SALT = {
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
    };
    private static final String ALGORITHM = "PBEWithMD5AndDES";
    private static final String CHARSET_NAME = "UTF-8";
    private static final int ITERATION_COUNT = 20;

    public static String encrypt(String property) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance(ALGORITHM);
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, ITERATION_COUNT));
        return base64Encode(pbeCipher.doFinal(property.getBytes(CHARSET_NAME)));
    }

    private static String base64Encode(byte[] bytes) {
        String base = Base64.encodeToString(bytes, 0);
        return base.substring(0, base.length() - 1);
    }
}
