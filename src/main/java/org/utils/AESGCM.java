package org.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AESGCM {
    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;

    private final byte[] aeskey;

    public AESGCM (byte[] aeskey) {
        this.aeskey = aeskey;
    }

    /*public String decrypt(String cipherText, String ivNonce) {
        try {
            return encryptDecrypt(cipherText, Cipher.DECRYPT_MODE, ivNonce);
        } catch (Exception e) {
            System.out.println("Failed to decrypt text");
        }

        return null;
    }*/

    /*
    public String encrypt(String plaintext) {
        try {
            return encryptDecrypt(plaintext, Cipher.ENCRYPT_MODE, null);
        } catch (Exception e) {
            System.out.println("Failed to encrypt text " + e.getMessage());
        }

        return null;
    }*/

    public String encrypt(String plaintext) {
            try {
            // Generate a secure random nonce
            SecureRandom random = new SecureRandom();
            byte[] nonce = new byte[GCM_IV_LENGTH];
            random.nextBytes(nonce);

            // Convert the key string to a SecretKey object
            SecretKey key = new SecretKeySpec(aeskey, "AES");

            // Initialize the cipher with the key and nonce
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            // Encrypt the plaintext
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // Concatenate the nonce, ciphertext, and tag into a single byte array
            byte[] encrypted = new byte[GCM_IV_LENGTH + ciphertext.length + GCM_TAG_LENGTH];
            System.arraycopy(nonce, 0, encrypted, 0, GCM_IV_LENGTH);
            System.arraycopy(ciphertext, 0, encrypted, GCM_IV_LENGTH, ciphertext.length);
            byte[] dummytag = new byte[16];
            System.arraycopy(dummytag, 0, encrypted, GCM_IV_LENGTH + ciphertext.length, GCM_TAG_LENGTH);

            // Encode the result as a Base64 string
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
                System.out.println("Failed to encrypt text " + e.getMessage());
            return  null;
        }
    }

    public String decrypt(String encrypted) {
        try {
            // Decode the Base64 string into a byte array
            byte[] encryptedBytes = Base64.getDecoder().decode(encrypted);

            // Extract the nonce, ciphertext, and tag from the encrypted bytes
            byte[] nonce = new byte[GCM_IV_LENGTH];
            byte[] ciphertext = new byte[encryptedBytes.length - GCM_IV_LENGTH - GCM_TAG_LENGTH];
            byte[] tag = new byte[GCM_TAG_LENGTH];

            //copie des informations dans les bonnes variables
            System.arraycopy(encryptedBytes, 0, nonce, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedBytes, GCM_IV_LENGTH, ciphertext, 0, ciphertext.length);
            System.arraycopy(encryptedBytes, GCM_IV_LENGTH + ciphertext.length, tag, 0, GCM_TAG_LENGTH);

            SecretKey key = new SecretKeySpec(aeskey, "AES");

            // Initialize the cipher with the key and nonce
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            // Decrypt the ciphertext
            byte[] plaintext = cipher.doFinal(ciphertext);

            // Convert the decrypted bytes to a string
            return new String(plaintext, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Failed to decrypt text " + e.getMessage());
            return  null;
        }
    }

    /*private String encryptDecrypt(String cipherText, int cipherMode, String ivNonce) throws Exception {
        //TODO faire commentaire
        //
        byte[] IV = new byte[GCM_IV_LENGTH]; //todo genere aléatoirement ?

        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(aeskey.getEncoded(), "AES");

        if(ivNonce != null) {
            IV = ivNonce.getBytes();
        } else {
            random.nextBytes(IV);
        }

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        // Initialize Cipher
        cipher.init(cipherMode, keySpec, gcmParameterSpec);

        byte[] cypherBytes = new byte[0];
        if(cipherMode == 1) {
            cypherBytes = Base64.getEncoder().encode(cipherText.getBytes("UTF-8"));
        } else if(cipherMode == 2) {
            cypherBytes = Base64.getDecoder().decode(cipherText.getBytes("UTF-8"));
        }
        System.out.println("ciphertext : " + cipherText);
        System.out.println("nonceIV : " + Arrays.toString(IV));
        // Perform Decryption
        byte[] finalText = cipher.doFinal(cypherBytes);

        System.out.println("out : " + String.format("%S %S", new String(IV, StandardCharsets.UTF_8), new String(finalText, StandardCharsets.UTF_8)));

        System.out.println("ciphermode : " + cipherMode);
        System.out.println("base64 decoded result : " + Arrays.toString(Base64.getDecoder().decode(finalText)));

        //si encrypt alors on retourne le vecteur IV espace la chaine cryptée
        if(cipherMode == 1) {
            return String.format("%S %S", new String(IV, StandardCharsets.UTF_8), new String(finalText, StandardCharsets.UTF_8));
        }

        return new String(finalText);
    }*/
}
