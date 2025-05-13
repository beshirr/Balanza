package com.example.blanza;

/**
 * Manages the user session state within the current application runtime.
 * <p>
 * The SessionService provides an in-memory representation of the current user session,
 * maintaining the ID of the authenticated user and providing methods to access and
 * manipulate this session information. Unlike {@link SessionManager} which handles
 * persistent storage of sessions, SessionService focuses on managing the active
 * session during application execution.
 * <p>
 * This class follows a stateless service pattern with static methods, allowing any
 * component of the application to:
 * <ul>
 *   <li>Check if a user is currently logged in</li>
 *   <li>Retrieve the ID of the currently authenticated user</li>
 *   <li>Update the current session when a user logs in</li>
 *   <li>Clear the session when a user logs out</li>
 * </ul>
 * <p>
 * A key design characteristic of this service is that it maintains only the minimal
 * necessary information about the current user (the user ID), requiring other
 * components to use this ID to fetch additional user details as needed. This
 * approach helps maintain separation of concerns and avoids redundant storage
 * of user information.
 * <p>
 * Typical usage flow:
 * <ol>
 *   <li>Upon successful authentication, call {@link #setCurrentUserId(int)}</li>
 *   <li>Throughout the application, use {@link #getCurrentUserId()} to identify the user</li>
 *   <li>Before operations, verify {@link #isLoggedIn()} to ensure a valid session exists</li>
 *   <li>When logging out, call {@link #clear()} to terminate the session</li>
 * </ol>
 * 
 * @see SessionManager for persistent session storage between application launches
 */
public class SessionService {
    
    /**
     * The ID of the currently authenticated user.
     * <p>
     * This static field stores the user ID of the current session. A value of -1
     * indicates that no user is currently logged in (no active session).
     * <p>
     * This field is modified through the service methods rather than accessed directly,
     * providing controlled access to the session state.
     */
    private static int currentUserId = -1;

    /**
     * Retrieves the ID of the currently authenticated user.
     * <p>
     * This method returns the user ID for the active session, which can be used
     * by other components to:
     * <ul>
     *   <li>Load user-specific data from the database</li>
     *   <li>Apply user preferences</li>
     *   <li>Filter content based on user access rights</li>
     *   <li>Associate new records with the current user</li>
     * </ul>
     * <p>
     * Before using the returned ID, it's recommended to verify that a user is actually
     * logged in by calling {@link #isLoggedIn()}, as this method will return -1 if
     * no session is active.
     *
     * @return The ID of the currently authenticated user, or -1 if no user is logged in
     */
    public static int getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Sets the current user's ID, establishing a new session.
     * <p>
     * This method should be called when a user successfully authenticates to the
     * application. It updates the in-memory representation of the current session
     * to reflect the newly logged-in user.
     * <p>
     * Note that this method only updates the runtime session state; it does not
     * persist the session information for future application launches. To persist
     * the session, use {@link SessionManager#saveSession(int)} in conjunction with
     * this method.
     * <p>
     * Example usage in an authentication controller:
     * <pre>{@code
     * if (userCredentialsValid(username, password)) {
     *     User user = userDao.findByUsername(username);
     *     SessionService.setCurrentUserId(user.getId());
     *     SessionManager.saveSession(user.getId());  // For persistence
     *     // Redirect to main application screen
     * }
     * }</pre>
     *
     * @param userId The ID of the authenticated user to set as the current user
     */
    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }

    /**
     * Clears the current user session.
     * <p>
     * This method terminates the active session by resetting the current user ID
     * to -1, indicating that no user is logged in. It should be called when:
     * <ul>
     *   <li>A user explicitly logs out of the application</li>
     *   <li>The application detects that the current session is invalid</li>
     *   <li>A session timeout occurs</li>
     * </ul>
     * <p>
     * Note that this method only clears the in-memory session state; it does not
     * remove any persisted session information. To also clear persisted sessions,
     * use {@link SessionManager#clearSession()} in conjunction with this method.
     * <p>
     * Example usage in a logout controller:
     * <pre>{@code
     * public void handleLogout() {
     *     SessionService.clear();
     *     SessionManager.clearSession();
     *     // Redirect to login screen
     * }
     * }</pre>
     */
    public static void clear() {
        currentUserId = -1;
    }

    /**
     * Checks if a user is currently logged in.
     * <p>
     * This method verifies whether there is an active user session by checking
     * if the current user ID is valid (not -1). It's commonly used for:
     * <ul>
     *   <li>Conditional UI rendering based on authentication status</li>
     *   <li>Security checks before accessing protected features</li>
     *   <li>Redirecting unauthenticated users to the login screen</li>
     * </ul>
     * <p>
     * Example usage in a secure controller:
     * <pre>{@code
     * @FXML
     * public void initialize() {
     *     if (!SessionService.isLoggedIn()) {
     *         // Redirect to login or show authentication message
     *         try {
     *             SceneController.switchScene("login.fxml", "Login Required");
     *         } catch (IOException e) {
     *             e.printStackTrace();
     *         }
     *     }
     * }
     * }</pre>
     *
     * @return {@code true} if a user is currently logged in, {@code false} otherwise
     */
    public static boolean isLoggedIn() {
        return currentUserId != -1;
    }
}