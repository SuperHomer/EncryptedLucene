package com.testcodec.encryption;

import javax.crypto.SecretKey;

public interface EncryptionEngine {
    final static String KEY_FILEN_NAME = "key.env";
    public SecretKey generateKey(int keySize);
    public byte[] encrypt(byte[] input);
    public byte[] decrypt(byte[] input);
    public int getCipherSize(int inputSize);
}
