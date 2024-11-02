package com.example.rsa_encryp_decrypt.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class RsaEncryptDecryptService {

    public static Map<String, Object> map = new HashMap<>();
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void createKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keypair = keyPairGenerator.generateKeyPair();
            this.publicKey = keypair.getPublic();
            this.privateKey = keypair.getPrivate();
            map.put("publicKey", publicKey);
            map.put("privateKey", privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encryptMessage(String message) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String decryptMessage(String encryptedMessage) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
