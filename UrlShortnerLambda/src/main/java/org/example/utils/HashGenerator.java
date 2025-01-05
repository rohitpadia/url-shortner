package org.example.utils;

import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

public class HashGenerator {
    // private constructor to avoid instantiation
    private HashGenerator() {}

    private static final AtomicInteger counter = new AtomicInteger();

    public static String createHash(int length) {
        int count = counter.getAndIncrement();
        long timestamp = System.nanoTime();
        byte[] combined = new byte[16];

        // Combine timestamp and counter
        for (int i = 0; i < 8; i++) {
            combined[i] = (byte)(timestamp >>> (i * 8));
            combined[i + 8] = (byte)(count >>> (i * 8));
        }
        return Base64.getUrlEncoder().withoutPadding().encodeToString(combined).substring(0, length);
    }
}
