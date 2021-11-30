package com.testcodec.lucene86;

import com.testcodec.encryption.EncryptionEngine;
import org.apache.lucene.codecs.compressing.CompressionMode;
import org.apache.lucene.codecs.compressing.Compressor;
import org.apache.lucene.codecs.compressing.Decompressor;
import org.apache.lucene.store.ByteArrayDataInput;
import org.apache.lucene.store.DataInput;
import org.apache.lucene.store.GrowableByteArrayDataOutput;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;

public class EncryptedDecompressor extends Decompressor {

    private EncryptionEngine encryptionEngine;
    private CompressionMode compressionMode;
    private Decompressor decompressor;
    private long lastStartPointer = -1;

    public EncryptedDecompressor(EncryptionEngine encryptionEngine, CompressionMode compressionMode) {
        this.encryptionEngine = encryptionEngine;
        this.compressionMode = compressionMode;
        this.decompressor = compressionMode.newDecompressor();
    }

    @Override
    public void decompress(DataInput in, int originalLength, int offset, int length, BytesRef bytes) throws IOException {
        int encryptedLength = in.readVInt();
        BytesRef encryptedBytes = new BytesRef(512);
        if (encryptedLength > encryptedBytes.bytes.length) {
            encryptedBytes = new BytesRef(encryptedLength);
            //encryptedBytes.bytes = ArrayUtil.grow(encryptedBytes.bytes, encryptedLength);
        }

        //in.readBytes(encryptedBytes.bytes, offset, encryptedLength);
        in.readBytes(encryptedBytes.bytes, 0, encryptedLength);
        byte[] decryptedBytes = this.encryptionEngine.decrypt(encryptedBytes.bytes);
        ByteArrayDataInput decryptedDataInput = new ByteArrayDataInput(decryptedBytes, 0, encryptedBytes.length);
        decompressor.decompress(decryptedDataInput, originalLength, offset, length, bytes);

//        long fp = ((IndexInput) in).getFilePointer();
//        if (lastStartPointer != fp) { // we have not yet deciphered this new data block
//            lastStartPointer = fp;
//            int encryptLength = in.readVInt();
//            if (encryptLength > encryptedBytesBuffer.bytes.length) {
//                encryptedBytesBuffer.bytes = ArrayUtil.grow(encryptedBytesBuffer.bytes, encryptLength);
//            }
//            in.readBytes(encryptedBytesBuffer.bytes, 0, encryptLength);
//            try {
//                // we can reuse encryptedBytesBuffer as output since the decrypt method is copy-safe
//                encryptedBytesBuffer.length = decipher.doFinal(encryptedBytesBuffer.bytes, 0, encryptLength, encryptedBytesBuffer.bytes, 0);
//            } catch (Exception e) {
//                throw new IOException("Deciphering of compressed stored fields block failed", e);
//            }
//        }
//        else { // if we have already deciphered the data block, just skip it
//            IndexInput indexInput = (IndexInput) in;
//            int encryptLength = indexInput.readVInt();
//            indexInput.seek(indexInput.getFilePointer() + encryptLength);
//        }
//        ByteArrayDataInput input = new ByteArrayDataInput(encryptedBytesBuffer.bytes, 0, encryptedBytesBuffer.length);
//        decompressor.decompress(input, originalLength, offset, length, bytes);
    }

    @Override
    public Decompressor clone() {
        return new EncryptedDecompressor(this.encryptionEngine, this.compressionMode);
    }
}
