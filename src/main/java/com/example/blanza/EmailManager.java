package com.example.blanza;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailManager {
    private final String fromEmail;
    private final String password;
    private final Properties properties;

    public EmailManager(String fromEmail, String password) {
        this.fromEmail = fromEmail;
        this.password = password;

        this.properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
    }

    public void sendEmail(String toEmail, String subject, String body) {
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getInstance(properties, authenticator);
        try {
            Message mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(fromEmail));
            mail.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            mail.setSubject(subject);
            mail.setText(body);
            Transport.send(mail);

        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }
}
