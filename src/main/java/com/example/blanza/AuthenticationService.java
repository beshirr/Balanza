package com.example.blanza;

import io.github.cdimascio.dotenv.Dotenv;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * The type Authentication service.
 */
public class AuthenticationService {
    /**
     * Send otp void.
     *
     * @param email the email
     * @param OTP   the otp
     */
    public static void sendOTP(String email, String OTP) {
        Dotenv dotenv = Dotenv.configure().directory(".").load();
        String fromEmail = dotenv.get("EMAIL_SENDER");
        String password = dotenv.get("PASS");
        EmailManager emailManager = new EmailManager(fromEmail, password);
        emailManager.sendEmail(email, "Your Balanza verification code", "Hello " + email + " this is your verification code: " + OTP);
    }

    /**
     * Verify otp boolean.
     *
     * @param OTP   the otp
     * @return the boolean
     */
    public static boolean verifyOTP(String OTP){
        String requestedOTP = UserDB.getUserOTPByID(SessionManager.loadSession());
        return requestedOTP.equals(OTP);
    }
}
