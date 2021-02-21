package com.testcodec.lucene86;

import com.testcodec.encryption.EncryptionEngine;
import org.apache.lucene.codecs.StoredFieldsFormat;
import org.apache.lucene.codecs.StoredFieldsReader;
import org.apache.lucene.codecs.StoredFieldsWriter;
import org.apache.lucene.codecs.compressing.CompressingStoredFieldsFormat;
import org.apache.lucene.codecs.compressing.CompressionMode;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;

import java.io.IOException;

public class EncryptedLucene50StoredFieldsFormat extends FilterStoredFieldsFormat {

    private EncryptionEngine encryptionEngine;

    public EncryptedLucene50StoredFieldsFormat(StoredFieldsFormat storedFieldsFormat, EncryptionEngine encryptionEngine) {
        super(storedFieldsFormat);
        this.encryptionEngine = encryptionEngine;
    }

    @Override
    public StoredFieldsReader fieldsReader(Directory directory, SegmentInfo si, FieldInfos fn, IOContext context) throws IOException {
        EncryptedCompressionMode.setEncryptionEngine(this.encryptionEngine);
        return new CompressingStoredFieldsFormat("EncryptedLucene50StoredFieldsFastData", EncryptedCompressionMode.FAST, 1 << 14, 128, 10).fieldsReader(directory, si, fn, context);
    }

    @Override
    public StoredFieldsWriter fieldsWriter(Directory directory, SegmentInfo si, IOContext context) throws IOException {
        EncryptedCompressionMode.setEncryptionEngine(this.encryptionEngine);
        return new CompressingStoredFieldsFormat("EncryptedLucene50StoredFieldsFastData", EncryptedCompressionMode.FAST, 1 << 14, 128, 10).fieldsWriter(directory, si, context);
    }

}

