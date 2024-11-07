package com.ikonek.utils;

public class PasswordHashing {
    // NOTE: this algo is just for demo and could impose security threats
    private static final String SECRET_KEY = "my_secret_key@013579"; // just for demo, replace with complex one

    public String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty."); // Throw exception
        }

        StringBuilder hashedPassword = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            char keyChar = SECRET_KEY.charAt(i % SECRET_KEY.length());
            int hashedChar = (int) c ^ (int) keyChar;              // XOR operation
            hashedPassword.append(Integer.toHexString(hashedChar));   // Convert to hex
        }
        return hashedPassword.toString();
    }

    public boolean verifyPassword(String password, String storedHashedPassword) {
        if (password.isEmpty() || storedHashedPassword.isEmpty()) {
            return false;
        }
        return hashPassword(password).equals(storedHashedPassword);
    }
}