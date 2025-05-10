package com.example.blanza;

import java.io.*;

public class SessionManager {
    private static final String SESSION_FILE = "session.txt";

    public static void saveSession(int userId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SESSION_FILE))) {
            writer.write(String.valueOf(userId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int loadSession() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SESSION_FILE))) {
            return Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            return -1;  // No session found
        }
    }

    public static void clearSession() {
        File file = new File(SESSION_FILE);
        if (file.exists()) file.delete();
    }
}
