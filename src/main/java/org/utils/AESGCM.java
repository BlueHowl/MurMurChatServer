package org.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class AESGCM {
    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;

    private final byte[] aeskey;

    public AESGCM (byte[] aeskey) {
        this.aeskey = aeskey;
    }

    public AESGCM(String password, String salt) throws Exception {
        String cipher = "AES";
        this.aeskey = getPasswordBasedKey(cipher, password.toCharArray(), salt).getEncoded();
    }

    private static Key getPasswordBasedKey(String cipher, char[] password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] s = salt.getBytes(StandardCharsets.UTF_8);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password, s, 1000, 256);
        SecretKey pbeKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(pbeKeySpec);
        return new SecretKeySpec(pbeKey.getEncoded(), cipher);
    }

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
            byte[] encrypted = new byte[GCM_IV_LENGTH + ciphertext.length];
            System.arraycopy(nonce, 0, encrypted, 0, GCM_IV_LENGTH);
            System.arraycopy(ciphertext, 0, encrypted, GCM_IV_LENGTH, ciphertext.length);

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
            byte[] ciphertext = new byte[encryptedBytes.length - GCM_IV_LENGTH];

            //copie des informations dans les bonnes variables
            System.arraycopy(encryptedBytes, 0, nonce, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedBytes, GCM_IV_LENGTH, ciphertext, 0, ciphertext.length);

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
            e.printStackTrace();
            System.out.println("Failed to decrypt text " + e.getMessage());
            return  null;
        }
    }

}
