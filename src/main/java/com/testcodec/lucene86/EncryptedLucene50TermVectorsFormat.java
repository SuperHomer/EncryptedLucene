package com.testcodec.lucene86;

import org.apache.lucene.codecs.compressing.CompressingTermVectorsFormat;
import org.apache.lucene.codecs.compressing.CompressionMode;

public class EncryptedLucene50TermVectorsFormat extends CompressingTermVectorsFormat {

    public EncryptedLucene50TermVectorsFormat() {
        super("EncryptedLucene50TermVectorsData", "", EncryptedCompressionMode.FAST, 1 << 12, 10);
    }
}
