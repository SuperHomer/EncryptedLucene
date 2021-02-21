package com.testcodec.simpletext;

import com.testcodec.encryption.EncryptionEngine;
import org.apache.lucene.codecs.TermVectorsReader;
import org.apache.lucene.codecs.TermVectorsWriter;
import org.apache.lucene.codecs.simpletext.SimpleTextTermVectorsFormat;
import org.apache.lucene.codecs.simpletext.SimpleTextTermVectorsReader;
import org.apache.lucene.codecs.simpletext.SimpleTextTermVectorsWriter;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;

import java.io.IOException;

public class EncryptedSimpleTextTermVectorsFormat extends SimpleTextTermVectorsFormat {

    private EncryptionEngine encryptionEngine;

    public EncryptedSimpleTextTermVectorsFormat(EncryptionEngine encryptionEngine) {
        this.encryptionEngine = encryptionEngine;
    }

    @Override
    public TermVectorsReader vectorsReader(Directory directory, SegmentInfo segmentInfo, FieldInfos fieldInfos, IOContext context) throws IOException {
        return new SimpleTextTermVectorsReader(directory, segmentInfo, context);
    }

    @Override
    public TermVectorsWriter vectorsWriter(Directory directory, SegmentInfo segmentInfo, IOContext context) throws IOException {
        return new EncryptedSimpleTextTermVectorsWriter(directory, segmentInfo.name, context, this.encryptionEngine);
    }
}
