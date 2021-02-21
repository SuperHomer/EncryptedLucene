package com.testcodec.lucene86;

import com.testcodec.encryption.EncryptionEngine;
import org.apache.lucene.codecs.compressing.CompressionMode;
import org.apache.lucene.codecs.compressing.Compressor;
import org.apache.lucene.codecs.compressing.Decompressor;

public abstract class EncryptedCompressionMode extends CompressionMode {
    protected static EncryptionEngine encryptionEngine;

    public static void setEncryptionEngine(EncryptionEngine encryptionEngine) {
        EncryptedCompressionMode.encryptionEngine = encryptionEngine;
    }

    public static final CompressionMode FAST = new CompressionMode() {


        @Override
        public Compressor newCompressor() {
            return new EncryptedCompressor(encryptionEngine, CompressionMode.FAST);
        }


        @Override
        public Decompressor newDecompressor() {
            return new EncryptedDecompressor(encryptionEngine, CompressionMode.FAST);
        }
    };

}
