package com.testcodec.simpletext;

import com.testcodec.encryption.AESEngine;
import com.testcodec.encryption.EncryptionEngine;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.StoredFieldVisitor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptedStoredFieldVisitor extends StoredFieldVisitor {
    private StoredFieldVisitor delegate;
    private final EncryptionEngine encryptionEngine;

    public EncryptedStoredFieldVisitor(StoredFieldVisitor visitor, EncryptionEngine encryptionEngine) {
        this.delegate = visitor;
        this.encryptionEngine = encryptionEngine;
    }

    @Override
    public Status needsField(FieldInfo fieldInfo) throws IOException {
        return this.delegate.needsField(fieldInfo);
    }

    @Override
    public void stringField(FieldInfo fieldInfo, byte[] value) throws IOException {
        this.delegate.stringField(fieldInfo, encryptionEngine.decrypt(Base64.getDecoder().decode(value)));
    }
}
