package com.testcodec.lucene86;

import org.apache.lucene.codecs.FieldsConsumer;
import org.apache.lucene.codecs.NormsProducer;
import org.apache.lucene.codecs.PostingsWriterBase;
import org.apache.lucene.codecs.blocktree.BlockTreeTermsWriter;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.SegmentWriteState;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;

public class EncryptedBlockTreeTermsWriter extends FilterFieldsConsumer {
    public EncryptedBlockTreeTermsWriter(FieldsConsumer fieldsConsumer) {
        super(fieldsConsumer);
    }

    @Override
    public void write(Fields fields, NormsProducer norms) throws IOException {
        super.write(fields, norms);
    }
}
