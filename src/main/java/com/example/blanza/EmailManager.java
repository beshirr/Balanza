package com.example.blanza;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailManager {
    private final String fromEmail;
    private final String password;
    private final Properties properties;

    public EmailManager(String fromEmail, String password) {
        this.fromEmail = fromEmail;
        this.password = password;

        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
    }

    public void sendEmail(String toEmail, String subject, String body) {
        jakarta.mail.Session session = jakarta.mail.Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
