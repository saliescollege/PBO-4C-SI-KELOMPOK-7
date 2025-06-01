package login;

import java.util.ArrayList;

public class UserDatabase {
    // Jangan lupa deklarasi dan inisialisasi users
    private static ArrayList<User> users = new ArrayList<>();

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static User login(String username, String password) {
        for (User u : users) {
            if (u.username.equals(username) && u.password.equals(password)) {
                return u;
            }
        }
        return null;
    }

    public static boolean register(String username, String password, String role) {
        for (User u : users) {
            if (u.username.equals(username)) return false;
        }
        users.add(new User(username, password, role));
        return true;
    }

    // Cek apakah username sudah ada
    public static boolean isUsernameExists(String username) {
        for (User u : users) {
            if (u.username.equals(username)) {
                return true;
            }
        }
        return false;
    }

    // Update user berdasarkan username lama
    public static boolean updateUser(String oldUsername, String newUsername, String newPassword) {
        for (User u : users) {
            if (u.username.equals(oldUsername)) {
                u.username = newUsername;
                if (newPassword != null && !newPassword.isEmpty()) {
                    u.password = newPassword;
                }
                return true;
            }
        }
        return false;
    }
}
