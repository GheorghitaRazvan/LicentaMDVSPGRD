package com.licenta.scheduler.services;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;

@Service
public class Encrypter {
    public String encrypt(String data) {
        String b64Encoded = Arrays.toString(Base64.getEncoder().encode(data.getBytes()));

        String reversed = new StringBuffer(b64Encoded).reverse().toString();

        StringBuilder encrypted = new StringBuilder();

        final int CEASAR_OFFSET = 4;

        for(int i = 0; i < reversed.length(); i++)
        {
            encrypted.append(reversed.charAt(i) + CEASAR_OFFSET);
        }

        return encrypted.toString();
    }

    public String decode(String data) {
        StringBuilder decoded = new StringBuilder();
        final int CAESAR_OFFSET = 4;

        for(int i = 0; i < data.length(); i++)
        {
            decoded.append(data.charAt(i) - CAESAR_OFFSET);
        }

        String reversed = new StringBuffer(decoded.toString()).reverse().toString();

        return Arrays.toString(Base64.getEncoder().encode(reversed.getBytes()));
    }
}
