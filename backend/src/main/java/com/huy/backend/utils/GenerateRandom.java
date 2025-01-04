package com.huy.backend.utils;

import java.util.Random;

public class GenerateRandom {

    public static Long generateRandomLong() {
        Random random = new Random();
        // Generate a random number between 1 and 10000
        return random.nextLong(1, 10001);
    }
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }
}
