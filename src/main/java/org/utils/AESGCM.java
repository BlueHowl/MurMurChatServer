package org.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESGCM {
    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;
    private final SecretKey key;

    private final SecureRandom random;

    public AESGCM (SecretKey key) {
        this.key = key;
        random = new SecureRandom();
    }

    public String decrypt(String cipherText, String ivNonce) {
        try {
            return encryptDecrypt(cipherText, Cipher.DECRYPT_MODE, ivNonce);
        } catch (Exception e) {
            System.out.println("Failed to decrypt text");
        }

        return null;
    }

    public String encrypt(String plaintext) {
        try {
            return encryptDecrypt(plaintext, Cipher.ENCRYPT_MODE, null);
        } catch (Exception e) {
            System.out.println("Failed to encrypt text");
        }

        return null;
    }

    private String encryptDecrypt(String cipherText, int cipherMode, String ivNonce) throws Exception {
        //TODO faire commentaire
        //
        byte[] IV = new byte[GCM_IV_LENGTH]; //todo genere aléatoirement ?

        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        if(ivNonce != null) {
            IV = ivNonce.getBytes();
        } else {
            random.nextBytes(IV);
        }

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        // Initialize Cipher
        cipher.init(cipherMode, keySpec, gcmParameterSpec);

        //
        byte[] cypherBytes = Base64.getDecoder().decode(cipherText.getBytes("UTF-8"));

        // Perform Decryption
        byte[] finalText = cipher.doFinal(cypherBytes);

        //si encrypt alors on retourne le vecteur IV espace la chaine cryptée
        if(cipherMode == 1) {
            return String.format("%S %S", new String(IV), new String(finalText));
        }

        return new String(finalText);
    }
}
