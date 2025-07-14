package com.forecasting.ai;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public static void sendMail(String test) {
        // Sender's email and password
        final String username = "manu567999@gmail.com";
        final String password = "rntqewbdkfqkjblj"; // Consider using app passwords for Gmail

        // Recipient's email
        String toEmail = "lmanojkum99@gmail.com";
//        toEmail="abhinav65vrma@gmail.com";
//        toEmail="gaidhani.jyoti@gmail.com";
//        toEmail="vaibhav.manwatkar@gmail.com";

        // Setup mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP host for Gmail
        props.put("mail.smtp.port", "587"); // Port for Gmail

        // Create session
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("ForeCasting");
            message.setText(test);

            // Send email
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}