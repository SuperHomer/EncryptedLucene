//package com.testcodec.encryption;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import java.security.*;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//
//public class RSAEngine implements EncryptionEngine {
//
//    private final byte[] privateKey;
//    private final byte[] publicKey;
//
//    private final int inputByteSizeLimit = 53;
//
//    public RSAEngine(String privateKey, String publicKey) {
//        this.privateKey = Base64.getDecoder().decode(privateKey);
//        this.publicKey = Base64.getDecoder().decode(publicKey);
//    }
//
//    @Override
//    public byte[] encrypt(byte[] input) {
////        if (input.length > inputByteSizeLimit) {
////
////        }
//        Cipher c = null;
//        try {
//            c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//            c.init(Cipher.ENCRYPT_MODE, getPublicKey());
//            byte[] encryptedData = c.doFinal(input);
//            return encryptedData;
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        }
//        return new byte[0];
//    }
//
////    private int separate
////
////    private String enrcryptBlock(byte[] input) {
////
////    }
//
//    @Override
//    public byte[] decrypt(byte[] input) {
//        Cipher cipher = null;
//        try {
//            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
//            return cipher.doFinal(input);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        }
//        return new byte[0];
//    }
//
//    public PublicKey getPublicKey(){
//        PublicKey publicKey = null;
//        try{
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(this.publicKey);
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            publicKey = keyFactory.generatePublic(keySpec);
//            return publicKey;
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//        return publicKey;
//    }
//
//    public PrivateKey getPrivateKey(){
//        PrivateKey privateKey = null;
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(this.privateKey);
//        KeyFactory keyFactory = null;
//        try {
//            keyFactory = KeyFactory.getInstance("RSA");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        try {
//            privateKey = keyFactory.generatePrivate(keySpec);
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//        return privateKey;
//    }
//}
