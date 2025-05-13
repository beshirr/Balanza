package com.example.blanza;

public class SessionService {
    private static int currentUserId = -1;

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }

    public static void clear() {
        currentUserId = -1;
    }

    public static boolean isLoggedIn() {
        return currentUserId != -1;
    }
}

