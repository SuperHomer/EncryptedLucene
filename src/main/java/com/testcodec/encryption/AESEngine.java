package com.testcodec.encryption;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.FieldInfo;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

// https://www.baeldung.com/java-aes-encryption-decryption
public class AESEngine implements EncryptionEngine {

    private static String algorithm = "AES";
    private final int keySize = 256;
    private SecretKey key;
    private static String keyPath;

    public AESEngine(String indexPath) {
        if (indexPath == null) { // Creating codec from segment
            try {
                this.key = new SecretKeySpec(readKeyFromFilePath(), algorithm);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            this.keyPath = indexPath+"/"+KEY_FILEN_NAME;
            File keyFile = new File(keyPath);
            if (keyFile.exists()) {
                try {
                    this.key = new SecretKeySpec(readKeyFromFilePath(), algorithm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.key = generateKey(keySize);
                storeKey();
            }
        }
    }


    public byte[] readKeyFromFilePath() throws Exception {
        File keyFile = new File(keyPath);
        if (keyFile.exists()) {
            try
            {
                List<String> lines = FileUtils.readLines(keyFile, "UTF-8");
                for (String line : lines) {
                    return Base64.getDecoder().decode(line);
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new Exception("Error no key file found");
        }
        return null;
    }

    public void storeKey() {
        // TODO: Add key encryption with MK
        try {
            Files.writeString(Path.of(keyPath), Base64.getEncoder().encodeToString(key.getEncoded()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: Add iv in cipher init
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public int getCipherSize(int inputSize) {
        return inputSize + (16 - (inputSize % 16));
    }

    @Override
    public SecretKey generateKey(int keySize) {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGenerator.init(keySize);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    @Override
    public byte[] encrypt(byte[] input) {
        Cipher c = null;
        try {
            c = Cipher.getInstance(algorithm);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = c.doFinal(input);
            return encryptedData;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }
// Base64.getEncoder().encodeToString
    @Override
    public byte[] decrypt(byte[] input) {
        Cipher c = null;
        try {
            c = Cipher.getInstance(algorithm);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] data = c.doFinal(input);
            return data;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }
}
