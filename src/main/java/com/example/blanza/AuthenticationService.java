package com.example.blanza;

import javax.mail.*;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class AuthenticationService {
    public static Void sendOTP(String email, String OTP){
        String senderEmail = "blanzaauth@gmail.com";
        String senderPassword = "BalanzaSoftware";
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        try {
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(senderEmail));
            mail.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            mail.setSubject("Your Balanza verification code");
            mail.setText("Hello " + email + " this is your verification code: " + OTP);
            Transport.send(mail);
        } catch (MessagingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verifyOTP(String email, String OTP){
        UserInfo userInfo = UserDB.getUserInfoByEmail(email);
        String requestedOTP = userInfo.getOtp();
        return requestedOTP.equals(OTP);
    }
}
