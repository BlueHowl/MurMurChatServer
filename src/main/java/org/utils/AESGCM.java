package org.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESGCM {
    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;
    private final SecretKey key;

    public AESGCM (SecretKey key) {
        this.key = key;
    }

    public String decrypt(String cipherText) throws Exception {
        return encryptDecrypt(cipherText, Cipher.DECRYPT_MODE);
    }

    public String encrypt(String plaintext) throws Exception {
        return encryptDecrypt(plaintext, Cipher.ENCRYPT_MODE);
    }

    private String encryptDecrypt(String cipherText, int cipherMode) throws Exception {
        //TODO faire commentaire
        //
        byte[] IV = new byte[GCM_IV_LENGTH];

        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        // Initialize Cipher for DECRYPT_MODE
        cipher.init(cipherMode, keySpec, gcmParameterSpec);

        //
        byte[] cypherBytes = Base64.getDecoder().decode(cipherText.getBytes("UTF-8"));

        // Perform Decryption
        byte[] finalText = cipher.doFinal(cypherBytes);

        return new String(finalText);
    }
}
