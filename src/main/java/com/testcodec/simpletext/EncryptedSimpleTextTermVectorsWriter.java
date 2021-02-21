package com.testcodec.simpletext;

import com.testcodec.encryption.EncryptionEngine;
import org.apache.lucene.codecs.simpletext.SimpleTextTermVectorsWriter;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.nio.ByteBuffer;

public class EncryptedSimpleTextTermVectorsWriter extends SimpleTextTermVectorsWriter {

    private EncryptionEngine encryptionEngine;

    public EncryptedSimpleTextTermVectorsWriter(Directory directory, String segment, IOContext context, EncryptionEngine encryptionEngine) throws IOException {
        super(directory, segment, context);
        this.encryptionEngine = encryptionEngine;
    }

    @Override
    public void startDocument(int numVectorFields) throws IOException {
        super.startDocument(numVectorFields);
    }

//    @Override
//    public void startField(FieldInfo info, int numTerms, boolean positions, boolean offsets, boolean payloads) throws IOException {
//        IndexOptions indexOptions = info.getIndexOptions();
//        FieldInfo encryptedInfo = new FieldInfo(this.encryptionEngine.encrypt(info.name.getBytes()), info.number, info.hasVectors(), info.omitsNorms(), info.hasPayloads(), info.getIndexOptions(), info.getDocValuesType(), info.getDocValuesGen(), info.attributes(), info.getPointDimensionCount(), info.getPointIndexDimensionCount(), info.getPointNumBytes(), info.isSoftDeletesField());
//        super.startField(encryptedInfo, numTerms, positions, offsets, payloads);
//    }

    @Override
    public void startTerm(BytesRef term, int freq) throws IOException {
        super.startTerm(term, freq);
    }

    @Override
    public void addPosition(int position, int startOffset, int endOffset, BytesRef payload) throws IOException {
//        int encryptedPosition = Integer.parseInt(this.encryptionEngine.encrypt(ByteBuffer.allocate(4).putInt(position).array()));
        super.addPosition(position, startOffset, endOffset, payload);
    }
}
