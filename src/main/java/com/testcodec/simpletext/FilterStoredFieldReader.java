package com.testcodec.simpletext;

import org.apache.lucene.codecs.StoredFieldsReader;
import org.apache.lucene.index.StoredFieldVisitor;
import org.apache.lucene.util.Accountable;

import java.io.IOException;
import java.util.Collection;

public abstract class FilterStoredFieldReader extends StoredFieldsReader {
    protected final StoredFieldsReader delegate;

    protected FilterStoredFieldReader(StoredFieldsReader delegate) {
        this.delegate = delegate;
    }

    @Override
    public StoredFieldsReader getMergeInstance() {
        return this.delegate.getMergeInstance();
    }

    @Override
    public void visitDocument(int docID, StoredFieldVisitor visitor) throws IOException {
        this.delegate.visitDocument(docID, visitor);
    }

    @Override
    public StoredFieldsReader clone() {
        return this.delegate.clone();
    }

    @Override
    public void checkIntegrity() throws IOException {
        this.delegate.checkIntegrity();
    }

    @Override
    public Collection<Accountable> getChildResources() {
        return this.delegate.getChildResources();
    }

    @Override
    public void close() throws IOException {
        this.delegate.close();
    }

    @Override
    public long ramBytesUsed() {
        return this.delegate.ramBytesUsed();
    }

}
