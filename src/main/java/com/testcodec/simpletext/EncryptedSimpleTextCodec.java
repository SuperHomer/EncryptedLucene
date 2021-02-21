package com.testcodec.simpletext;

import com.testcodec.encryption.AESEngine;
import com.testcodec.encryption.EncryptionEngine;
import org.apache.lucene.codecs.FilterCodec;
//import org.apache.lucene.codecs.StoredFieldsFormat;
import org.apache.lucene.codecs.StoredFieldsFormat;
import org.apache.lucene.codecs.TermVectorsFormat;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;

public class EncryptedSimpleTextCodec extends FilterCodec {

    public static EncryptionEngine encryptionEngine;

    public EncryptedSimpleTextCodec() {
        super("EncryptedSimpleTextCodec", new SimpleTextCodec());
    }

    public void setEncryptionEngine(EncryptionEngine encryptionEngine) {
        this.encryptionEngine = encryptionEngine;
    }

    @Override
    public StoredFieldsFormat storedFieldsFormat() {
        return new EncryptedSimpleTextStoredField(encryptionEngine);
    }

    @Override
    public TermVectorsFormat termVectorsFormat() {
        return new EncryptedSimpleTextTermVectorsFormat(encryptionEngine);
    }
}
