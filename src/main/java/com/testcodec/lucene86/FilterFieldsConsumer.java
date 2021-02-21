package com.testcodec.lucene86;

import org.apache.lucene.codecs.FieldsConsumer;
import org.apache.lucene.codecs.NormsProducer;
import org.apache.lucene.index.Fields;

import java.io.IOException;

public class FilterFieldsConsumer extends FieldsConsumer {

    protected FieldsConsumer delegate;

    public FilterFieldsConsumer(FieldsConsumer fieldsConsumer) {
        this.delegate = fieldsConsumer;
    }

    @Override
    public void write(Fields fields, NormsProducer norms) throws IOException {
        this.delegate.write(fields, norms);
    }

    @Override
    public void close() throws IOException {
        this.delegate.close();
    }
}
