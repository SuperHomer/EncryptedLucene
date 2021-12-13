package com.testcodec.lucene86;

import com.testcodec.encryption.EncryptionEngine;
import org.apache.lucene.codecs.*;
import org.apache.lucene.codecs.blocktree.BlockTreeTermsReader;
import org.apache.lucene.codecs.blocktree.BlockTreeTermsWriter;
import org.apache.lucene.codecs.lucene84.Lucene84PostingsReader;
import org.apache.lucene.codecs.lucene84.Lucene84PostingsWriter;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.index.SegmentWriteState;
import org.apache.lucene.util.IOUtils;

import java.io.IOException;

public class EncryptedLucene84PostingsFormat extends FilterPostingsFormat {
    private EncryptionEngine encryptionEngine;

    public EncryptedLucene84PostingsFormat(String name, PostingsFormat postingsFormat, EncryptionEngine encryptionEngine) {
        super(name, postingsFormat);
        this.encryptionEngine = encryptionEngine;
    }

    @Override
    public FieldsConsumer fieldsConsumer(SegmentWriteState state) throws IOException {
        PostingsWriterBase postingsWriter = new Lucene84PostingsWriter(state);
        boolean success = false;
        try {
            FieldsConsumer ret = new EncryptedBlockTreeTermsWriter(state,
                    postingsWriter,
                    EncryptedBlockTreeTermsWriter.DEFAULT_MIN_BLOCK_SIZE,
                    EncryptedBlockTreeTermsWriter.DEFAULT_MAX_BLOCK_SIZE,
                    this.encryptionEngine);
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
        PostingsReaderBase postingsReader = new Lucene84PostingsReader(state);
        boolean success = false;
        try {
            FieldsProducer ret = new EncryptedBlockTreeTermsReader(postingsReader, state);
            success = true;
            return ret;
        } finally {
            if (!success) {
                IOUtils.closeWhileHandlingException(postingsReader);
            }
        }
        //return super.fieldsProducer(state);
    }



}
