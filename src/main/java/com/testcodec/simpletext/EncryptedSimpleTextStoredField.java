package com.testcodec.simpletext;

import com.testcodec.encryption.EncryptionEngine;
import org.apache.lucene.codecs.StoredFieldsReader;
import org.apache.lucene.codecs.StoredFieldsWriter;
import org.apache.lucene.codecs.simpletext.SimpleTextStoredFieldsFormat;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;

import java.io.IOException;

public class EncryptedSimpleTextStoredField extends SimpleTextStoredFieldsFormat {

    private EncryptionEngine encryptionEngine;

    public EncryptedSimpleTextStoredField(EncryptionEngine encryptionEngine) {
        this.encryptionEngine = encryptionEngine;
    }


    @Override
    public StoredFieldsReader fieldsReader(Directory directory, SegmentInfo si, FieldInfos fn, IOContext context) throws IOException {
        return new EncryptedStoredFieldReader(directory, si, fn, context, this.encryptionEngine);
    }

    @Override
    public StoredFieldsWriter fieldsWriter(Directory directory, SegmentInfo si, IOContext context) throws IOException {
        return new EncryptedStoredFieldWriter(directory, si.name, context, this.encryptionEngine);
    }
}
