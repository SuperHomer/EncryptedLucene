package com.testcodec.lucene86;

import org.apache.lucene.codecs.FieldsConsumer;
import org.apache.lucene.codecs.FieldsProducer;
import org.apache.lucene.codecs.PostingsFormat;
import org.apache.lucene.codecs.StoredFieldsFormat;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.index.SegmentWriteState;

import java.io.IOException;

public class FilterPostingsFormat extends PostingsFormat {

    protected final PostingsFormat delegate;

    public FilterPostingsFormat(String name, PostingsFormat postingsFormat) {
        super(name);
        this.delegate = postingsFormat;
    }


    @Override
    public FieldsConsumer fieldsConsumer(SegmentWriteState state) throws IOException {
        return this.delegate.fieldsConsumer(state);
    }

    @Override
    public FieldsProducer fieldsProducer(SegmentReadState state) throws IOException {
        return this.delegate.fieldsProducer(state);
    }
}
