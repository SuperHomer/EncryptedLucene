package com.testcodec.simpletext;

import com.testcodec.encryption.AESEngine;
import org.apache.lucene.codecs.simpletext.SimpleTextStoredFieldsWriter;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.testcodec.encryption.EncryptionEngine;

//public class EncryptedStoredFieldWriter extends FilterStoredFieldWriter {
//
//    private final EncryptionEngine encryptionEngine;
//
//    protected EncryptedStoredFieldWriter(Directory directory, String segment, IOContext context, EncryptionEngine encryptionEngine) throws IOException {
//        super(new SimpleTextStoredFieldsWriter(directory, segment, context));
//        this.encryptionEngine = encryptionEngine;
//    }
//
//    @Override
//    public void writeField(FieldInfo info, IndexableField field) throws IOException {
//        Field encryptedField = new Field(info.name, field.stringValue(), field.fieldType());
//        encryptedField.setStringValue(encryptionEngine.encrypt(encryptedField.stringValue().getBytes()));
//        super.writeField(info, encryptedField);
//    }
//
//}

public class EncryptedStoredFieldWriter extends SimpleTextStoredFieldsWriter {

    private final EncryptionEngine encryptionEngine;

    public EncryptedStoredFieldWriter(Directory directory, String segment, IOContext context, EncryptionEngine encryptionEngine) throws IOException {
        super(directory, segment, context);
        this.encryptionEngine = encryptionEngine;
    }

    public void writeField(FieldInfo info, IndexableField field) throws IOException {
        Field encryptedField = new Field(info.name, field.stringValue(), field.fieldType());
        encryptedField.setStringValue(Base64.getEncoder().encodeToString(encryptionEngine.encrypt(encryptedField.stringValue().getBytes())));
        super.writeField(info, encryptedField);
    }

}
