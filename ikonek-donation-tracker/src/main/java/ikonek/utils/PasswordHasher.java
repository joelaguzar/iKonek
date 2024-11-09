package ikonek.utils;

public class PasswordHasher {
    // NOTE: This algorithm is for demonstration purposes only and has security limitations.
    private static final String SECRET_KEY = "my_secret_key@013579"; // Demo key, replace with a more complex one if needed

    public String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty."); // Throw exception for invalid input
        }

        StringBuilder hashedPassword = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            char keyChar = SECRET_KEY.charAt(i % SECRET_KEY.length());
            int hashedChar = c ^ keyChar;                // XOR operation with the secret key
            hashedPassword.append(Integer.toHexString(hashedChar)); // Convert to hex for readable storage
        }
        return hashedPassword.toString();
    }

    public boolean verifyPassword(String password, String storedHashedPassword) {
        if (password == null || password.isEmpty() || storedHashedPassword == null || storedHashedPassword.isEmpty()) {
            return false;
        }
        return hashPassword(password).equals(storedHashedPassword); // Compare hashed values
    }
}
