package com.example.blanza;

import io.github.cdimascio.dotenv.Dotenv;


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
     * @return the void
     */
    public static void sendOTP(String email, String OTP){
//        String senderEmail = "blanzaauth@gmail.com";
//        String senderPassword = "euzavaleirdudutp";
//        Properties properties = new Properties();
//        properties.put("mail.smtp.host", "smtp.gmail.com");
//        properties.put("mail.smtp.port", "587");
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");
//        Session session = Session.getInstance(properties, new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(senderEmail, senderPassword);
//            }
//        });
//        try {
//            MimeMessage mail = new MimeMessage(session);
//            mail.setFrom(new InternetAddress(senderEmail));
//            mail.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
//            mail.setSubject("Your Balanza verification code");
//            mail.setText("Hello " + email + " this is your verification code: " + OTP);
//            Transport.send(mail);
//        } catch (MessagingException e){
//            e.printStackTrace();
//        }
//        return null;
        Dotenv dotenv = Dotenv.configure().directory(".").load();
        String body = "Hello" + email + " this is your verification code: " + OTP;
        String subject = "Your Balanza verification code";
        String fromEmail = dotenv.get("EMAIL_SENDER");
        String password = dotenv.get("PASS");
        EmailManager emailManager = new EmailManager(email, password);
        emailManager.sendEmail(fromEmail, subject, body);
    }

    /**
     * Verify otp boolean.
     *
     * @param email the email
     * @param OTP   the otp
     * @return the boolean
     */
    public static boolean verifyOTP(String email, String OTP){
        UserInfo userInfo = UserDB.getUserInfoByEmail(email);
        String requestedOTP = userInfo.getOtp();
        return requestedOTP.equals(OTP);
    }
}
