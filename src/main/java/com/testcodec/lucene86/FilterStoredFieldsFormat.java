package com.testcodec.lucene86;

import org.apache.lucene.codecs.StoredFieldsFormat;
import org.apache.lucene.codecs.StoredFieldsReader;
import org.apache.lucene.codecs.StoredFieldsWriter;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;

import java.io.IOException;

public class FilterStoredFieldsFormat extends StoredFieldsFormat {

    protected final StoredFieldsFormat delegate;

    public FilterStoredFieldsFormat(StoredFieldsFormat storedFieldsFormat) {
        this.delegate = storedFieldsFormat;
    }


    @Override
    public StoredFieldsReader fieldsReader(Directory directory, SegmentInfo si, FieldInfos fn, IOContext context) throws IOException {
        return this.delegate.fieldsReader(directory, si, fn, context);
    }

    @Override
    public StoredFieldsWriter fieldsWriter(Directory directory, SegmentInfo si, IOContext context) throws IOException {
        return this.delegate.fieldsWriter(directory, si, context);
    }
}
