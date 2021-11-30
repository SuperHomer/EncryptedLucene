package com.testcodec.lucene86;

import com.testcodec.encryption.AESEngine;
import com.testcodec.encryption.EncryptionEngine;
import org.apache.lucene.codecs.compressing.CompressionMode;
import org.apache.lucene.codecs.compressing.Compressor;
import org.apache.lucene.store.DataOutput;
import org.apache.lucene.store.GrowableByteArrayDataOutput;
import org.apache.lucene.util.ArrayUtil;

import java.io.IOException;

public class EncryptedCompressor extends Compressor {

    private EncryptionEngine encryptionEngine;
    private CompressionMode compressionMode;
    private Compressor compressor;
//    private GrowableByteArrayDataOutput compressedBytes = new GrowableByteArrayDataOutput(512);

    public EncryptedCompressor(EncryptionEngine encryptionEngine, CompressionMode compressionMode) {
        this.encryptionEngine = encryptionEngine;
        this.compressionMode = compressionMode;
        this.compressor = compressionMode.newCompressor();
    }


    @Override
    public void compress(byte[] bytes, int off, int len, DataOutput out) throws IOException {
        //compressedBytes.reset();
        GrowableByteArrayDataOutput compressedBytes = new GrowableByteArrayDataOutput(512);
        compressor.compress(bytes, off, len, compressedBytes);

        byte[] encryptedBytes = this.encryptionEngine.encrypt(compressedBytes.getBytes());


        out.writeVInt(encryptedBytes.length);
        //System.out.println("ENC size: "+encryptedBytes.length);
        //out.writeBytes(encryptedBytes, off, encryptedBytes.length);
        out.writeBytes(encryptedBytes, 0, encryptedBytes.length);
    }

    @Override
    public void close() throws IOException {
    }
}
