package com.example.blanza;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Service class responsible for handling authentication-related operations.
 * 
 * This class provides methods for generating, sending, and verifying one-time passwords (OTPs)
 * as part of the user authentication and verification workflow. It utilizes environment variables
 * to securely manage email credentials for sending verification codes.
 */
public class AuthenticationService {
    /**
     * Sends a one-time password (OTP) to the specified email address.
     * 
     * This method loads email credentials from environment variables,
     * creates an email manager instance, and sends the OTP to the user's
     * email address with appropriate subject and message content.
     *
     * @param email the recipient's email address
     * @param OTP   the one-time password to be sent to the user
     */
    public static void sendOTP(String email, String OTP) {
        Dotenv dotenv = Dotenv.configure().directory(".").load();
        String fromEmail = dotenv.get("EMAIL_SENDER");
        String password = dotenv.get("PASS");
        EmailManager emailManager = new EmailManager(fromEmail, password);
        emailManager.sendEmail(email, "Your Balanza verification code", "Hello " + email + " this is your verification code: " + OTP);
    }

    /**
     * Verifies if the provided OTP matches the one stored for the current user session.
     * 
     * This method retrieves the expected OTP for the current user from the database
     * and compares it with the OTP provided by the user during verification.
     *
     * @param OTP   the one-time password entered by the user for verification
     * @return      true if the provided OTP matches the stored OTP, false otherwise
     */
    public static boolean verifyOTP(String OTP){
        String requestedOTP = UserDB.getUserOTPByID(SessionManager.loadSession());
        return requestedOTP.equals(OTP);
    }
}