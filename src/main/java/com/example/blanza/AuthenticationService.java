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
    public static void sendOTP(String email, String OTP){
        String senderEmail = "blanzaauth@gmail.com";
        final String username = "balanzaauth@gmail.com";
        final String password = "euzavaleirdudutp";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getInstance(properties, authenticator);
        try {
            Message mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(senderEmail));
            mail.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            mail.setSubject("Your Balanza verification code");
            mail.setContent("Hello " + email + " this is your verification code: " + OTP, "text/html;charset=UTF-8");
            Transport.send(mail);
        } catch (MessagingException e){
            e.printStackTrace();
        }
//        Dotenv dotenv = Dotenv.configure().directory(".").load();
//        String body = "Hello" + email + " this is your verification code: " + OTP;
//        String subject = "Your Balanza verification code";
//        String fromEmail = dotenv.get("EMAIL_SENDER");
//        String password = dotenv.get("PASS");
//        EmailManager emailManager = new EmailManager(email, password);
//        emailManager.sendEmail(fromEmail, subject, body);
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
