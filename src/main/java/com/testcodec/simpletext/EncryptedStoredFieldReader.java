package com.testcodec.simpletext;

import com.testcodec.encryption.EncryptionEngine;
import org.apache.lucene.codecs.StoredFieldsReader;
import org.apache.lucene.codecs.simpletext.SimpleTextStoredFieldsReader;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.index.StoredFieldVisitor;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;

import java.io.IOException;


public class EncryptedStoredFieldReader extends FilterStoredFieldReader {

    private EncryptionEngine encryptionEngine;


    public EncryptedStoredFieldReader(Directory directory, SegmentInfo si, FieldInfos fn, IOContext context, EncryptionEngine encryptionEngine) throws IOException {
        super(new SimpleTextStoredFieldsReader(directory, si, fn, context));
        this.encryptionEngine = encryptionEngine;
    }

    public EncryptedStoredFieldReader(StoredFieldsReader storedFieldsReader, EncryptionEngine encryptionEngine) {
        super(storedFieldsReader);
        this.encryptionEngine = encryptionEngine;
    }

    @Override
    public void visitDocument(int docID, StoredFieldVisitor visitor) throws IOException {
        EncryptedStoredFieldVisitor encryptedVisitor = new EncryptedStoredFieldVisitor(visitor, this.encryptionEngine);
        super.visitDocument(docID, encryptedVisitor);
    }


    @Override
    public StoredFieldsReader clone() {
        StoredFieldsReader storedFieldsReader = this.delegate.clone();
        return new EncryptedStoredFieldReader(storedFieldsReader, this.encryptionEngine);
    }
}

