package com.example.rsa_encryp_decrypt.controller;

import com.example.rsa_encryp_decrypt.dto.MessageRequest;
import com.example.rsa_encryp_decrypt.dto.MessageResponse;
import com.example.rsa_encryp_decrypt.service.RsaEncryptDecryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RsaEncryptDecryptController {

    @Autowired

    private RsaEncryptDecryptService encryptDecryptService;

    @GetMapping("/create")
    public ResponseEntity<String> createPrivatePublicKeys() {
        encryptDecryptService.createKeyPair();
        return ResponseEntity.ok("RSA Key Pair generated successfully");
    }

    @PostMapping("/encrypt")
    public ResponseEntity<MessageResponse> encryptMessage(@RequestBody MessageRequest messageRequest) {
        try {
            String message = messageRequest.getMsisdn() + ":" + messageRequest.getName();
            String encryptedMessage = encryptDecryptService.encryptMessage(message);
            return ResponseEntity.ok(new MessageResponse(encryptedMessage));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error encrypting message: " + e.getMessage()));
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<MessageRequest> decryptMessage(@RequestBody String encryptedMessage) {
        try {
            String decryptedMessage = encryptDecryptService.decryptMessage(encryptedMessage);
            String[] parts = decryptedMessage.split(":", 2);
            String msisdn = parts[0];
            String name = parts[1];
            MessageRequest messageRequest = new MessageRequest(msisdn, name);
            return ResponseEntity.ok(messageRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
