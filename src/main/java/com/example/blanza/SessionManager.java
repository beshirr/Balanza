package com.example.blanza;

import java.io.*;

/**
 * Manages user session persistence in the Balanza application.
 * <p>
 * The SessionManager provides functionality to store, retrieve, and clear user session
 * information between application launches. It implements a simple file-based
 * persistence mechanism that saves the currently logged-in user's ID to a plain text file
 * on the local file system.
 * <p>
 * This class enables features such as:
 * <ul>
 *   <li>Automatic user login (remembering the last logged-in user)</li>
 *   <li>Session persistence across application restarts</li>
 *   <li>Session termination through explicit logout</li>
 * </ul>
 * <p>
 * All methods in this class are static, following a utility class pattern, to provide
 * global access to session management functionality throughout the application.
 * <p>
 * Implementation Note: This class uses a simple plaintext storage mechanism suitable for
 * development or applications with minimal security requirements. For production applications
 * handling sensitive user data, consider implementing a more secure session persistence
 * mechanism, such as encrypted storage or integration with OS-level credential managers.
 * 
 * @see SessionService
 */
public class SessionManager {
    
    /**
     * The name of the file where session information is stored.
     * <p>
     * This file is created in the application's working directory when a session
     * is saved, and contains only the user ID of the currently logged-in user.
     */
    private static final String SESSION_FILE = "session.txt";

    /**
     * Saves the current user's session to persistent storage.
     * <p>
     * This method writes the provided user ID to the session file, creating the file
     * if it doesn't exist or overwriting it if it does. This persists the user's login
     * state so the application can restore it on subsequent launches.
     * <p>
     * If an error occurs during the write operation, the exception is logged but not
     * propagated, meaning the method will silently fail rather than disrupt application
     * flow. This is appropriate for session persistence which is typically a non-critical
     * feature.
     *
     * @param userId The ID of the user whose session is being saved
     */
    public static void saveSession(int userId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SESSION_FILE))) {
            writer.write(String.valueOf(userId));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Loads the previously saved user session, if one exists.
     * <p>
     * This method attempts to read the user ID from the session file. If successful,
     * it returns the ID of the previously logged-in user, allowing the application
     * to automatically restore their session.
     * <p>
     * The method will return -1 in the following cases:
     * <ul>
     *   <li>The session file does not exist (no previous session)</li>
     *   <li>The session file is empty or corrupted</li>
     *   <li>An IO error occurs while reading the file</li>
     * </ul>
     * <p>
     * The application should interpret a return value of -1 as an indication that
     * no valid session exists and the user should be prompted to log in.
     *
     * @return The user ID from the saved session, or -1 if no valid session exists
     */
    public static int loadSession() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SESSION_FILE))) {
            return Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            return -1;  // No session found
        }
    }

    /**
     * Clears the current user session.
     * <p>
     * This method deletes the session file, effectively logging the user out of
     * the application. After calling this method, subsequent calls to {@link #loadSession()}
     * will return -1 until a new session is saved.
     * <p>
     * This method should be called when:
     * <ul>
     *   <li>A user explicitly logs out of the application</li>
     *   <li>The application detects that the current session is invalid (e.g., user deleted)</li>
     *   <li>Security policies require terminating the session</li>
     * </ul>
     * <p>
     * If the session file does not exist, this method does nothing and completes silently.
     */
    public static void clearSession() {
        File file = new File(SESSION_FILE);
        if (file.exists()) file.delete();
    }
}