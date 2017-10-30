package com.example.sebastiaan.sit207project2kana;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * The functionality of this was created using this tutorial by Sylvain Saurel
 * https://www.youtube.com/watch?v=kN8hlHO8US0
 */

// This class handles password encryption
public class PasswordEncrypt {

    // For encryption
    public String encrypt(String inPass, String inKey) throws Exception {
        // To encrypt the password, we need a key
        SecretKeySpec key = newKey(inKey);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedValue = cipher.doFinal(inPass.getBytes());
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT);
    }

    // For decryption
    public String decrypt(String inPass, String inKey) throws Exception {
        // We also need a key to decrypt
        SecretKeySpec key = newKey(inKey);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue = Base64.decode(inPass, Base64.DEFAULT);
        byte[] decryptVal = cipher.doFinal(decryptedValue);
        return new String(decryptVal);
    }

    // This is to generate a key for the key
    public SecretKeySpec newKey(String inKey) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Turn they key into a byte array
        byte[] bytes = inKey.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        // Create a new key
        return new SecretKeySpec(key, "AES");
    }
}
