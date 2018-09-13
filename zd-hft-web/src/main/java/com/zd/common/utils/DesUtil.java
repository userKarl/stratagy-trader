package com.zd.common.utils;


import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Component
public class DesUtil {
    private static byte[] iv1 = { (byte) 0x12, (byte) 0x34, (byte) 0x56,
            (byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };

    private static String decryptKey = "smith!@#";

    public static String decrypt(String decryptString) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(iv1);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return new String(cipher.doFinal(Base64.decode(decryptString)));
    }

    public static String encrypt(String encryptString) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(iv1);
        DESKeySpec dks = new DESKeySpec(decryptKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return Base64.encode(cipher.doFinal(encryptString.getBytes()));
    }

}
