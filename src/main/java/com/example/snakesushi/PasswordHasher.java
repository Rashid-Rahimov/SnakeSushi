package com.example.snakesushi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {

    private static final String PASSWORD = "1234";

    public static String hash(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.encode(password);
    }

    public static void main(String[] args) {
        System.out.println(hash(PASSWORD));
    }
}
