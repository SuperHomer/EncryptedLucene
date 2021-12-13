package com.testcodec.lucene86;

import com.testcodec.encryption.EncryptionEngine;
import org.apache.lucene.codecs.FilterCodec;
import org.apache.lucene.codecs.PostingsFormat;
import org.apache.lucene.codecs.StoredFieldsFormat;
import org.apache.lucene.codecs.TermVectorsFormat;
import org.apache.lucene.codecs.lucene50.Lucene50StoredFieldsFormat;
import org.apache.lucene.codecs.lucene50.Lucene50TermVectorsFormat;
import org.apache.lucene.codecs.lucene84.Lucene84PostingsFormat;
import org.apache.lucene.codecs.lucene86.Lucene86Codec;

public class EncryptedLucene86Codec extends FilterCodec {

    public static EncryptionEngine encryptionEngine;

    public EncryptedLucene86Codec() {
        super("EncryptedLucene86Codec", new Lucene86Codec());
    }

    public void setEncryptionEngine(EncryptionEngine encryptionEngine) {
        this.encryptionEngine = encryptionEngine;
    }

    @Override
    public StoredFieldsFormat storedFieldsFormat() {
        return new EncryptedLucene50StoredFieldsFormat(new Lucene50StoredFieldsFormat(), encryptionEngine);
    }

    /*@Override
    public TermVectorsFormat termVectorsFormat() {
        return new EncryptedLucene50TermVectorsFormat();
    }*/


    @Override
    public PostingsFormat postingsFormat() {
        // return new Lucene84PostingsFormat();//
        return new EncryptedLucene84PostingsFormat("EncryptedLucene84", new Lucene84PostingsFormat(), encryptionEngine);
    }
}
