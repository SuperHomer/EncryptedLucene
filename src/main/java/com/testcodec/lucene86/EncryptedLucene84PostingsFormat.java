package com.testcodec.lucene86;

import org.apache.lucene.codecs.FieldsConsumer;
import org.apache.lucene.codecs.FieldsProducer;
import org.apache.lucene.codecs.PostingsFormat;
import org.apache.lucene.codecs.PostingsWriterBase;
import org.apache.lucene.codecs.blocktree.BlockTreeTermsWriter;
import org.apache.lucene.codecs.lucene84.Lucene84PostingsWriter;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.index.SegmentWriteState;
import org.apache.lucene.util.IOUtils;

import java.io.IOException;

public class EncryptedLucene84PostingsFormat extends FilterPostingsFormat {
    public EncryptedLucene84PostingsFormat(String name, PostingsFormat postingsFormat) {
        super(name, postingsFormat);
    }

    @Override
    public FieldsConsumer fieldsConsumer(SegmentWriteState state) throws IOException {
        PostingsWriterBase postingsWriter = new Lucene84PostingsWriter(state);
        boolean success = false;
        try {
            FieldsConsumer ret = new EncryptedBlockTreeTermsWriter(new BlockTreeTermsWriter(state,
                    postingsWriter,
                    BlockTreeTermsWriter.DEFAULT_MIN_BLOCK_SIZE,
                    BlockTreeTermsWriter.DEFAULT_MAX_BLOCK_SIZE));
            success = true;
            return ret;
        } finally {
            if (!success) {
                IOUtils.closeWhileHandlingException(postingsWriter);
            }
        }
        //return super.fieldsConsumer(state);
    }

    @Override
    public FieldsProducer fieldsProducer(SegmentReadState state) throws IOException {
        return super.fieldsProducer(state);
    }
}
