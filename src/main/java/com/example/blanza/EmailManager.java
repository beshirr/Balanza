package com.example.blanza;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Manages email sending functionality for the Balanza application.
 * <p>
 * This class encapsulates the configuration and functionality needed to send
 * emails from the application to users. It uses the JavaMail API to connect to
 * an SMTP server (specifically configured for Gmail) and send emails with
 * custom subjects and body content.
 * <p>
 * The email sender credentials are provided during instantiation and stored
 * securely within the instance. Default SMTP properties are configured for
 * Gmail's SMTP server with TLS security.
 * <p>
 * Example usage:
 * <pre>
 * EmailManager emailManager = new EmailManager("app@example.com", "password123");
 * emailManager.sendEmail("user@example.com", "Payment Reminder", "Your payment is due tomorrow.");
 * </pre>
 */
public class EmailManager {
    /** Email address used as the sender */
    private final String fromEmail;
    
    /** Password for the sender's email account */
    private final String password;
    
    /** SMTP configuration properties */
    private final Properties properties;

    /**
     * Creates a new EmailManager with the specified sender credentials.
     * <p>
     * Initializes the SMTP configuration with default settings for Gmail:
     * <ul>
     *   <li>SMTP authentication enabled</li>
     *   <li>STARTTLS encryption enabled</li>
     *   <li>Gmail SMTP server (smtp.gmail.com)</li>
     *   <li>Port 587 (standard for TLS)</li>
     * </ul>
     *
     * @param fromEmail The email address to send emails from
     * @param password The password for the sender email account
     */
    public EmailManager(String fromEmail, String password) {
        this.fromEmail = fromEmail;
        this.password = password;

        this.properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
    }

    /**
     * Sends an email with the specified details using the configured SMTP settings.
     * <p>
     * This method creates an authenticated session with the SMTP server using
     * the sender credentials provided during instantiation. It then constructs
     * and sends a simple text email to the specified recipient.
     * <p>
     * If the email sending process encounters any issues, an error message is
     * printed to the console, but no exception is propagated to the caller.
     *
     * @param toEmail The recipient's email address
     * @param subject The subject line of the email
     * @param body The main content/body of the email
     */
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