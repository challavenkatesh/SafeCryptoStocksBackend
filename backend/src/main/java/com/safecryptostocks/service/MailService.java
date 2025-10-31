package com.safecryptostocks.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String senderEmail; // sender email from application.properties

    @Value("${spring.mail.password}")
    private String senderPassword;

    public void sendOtp(String recipientEmail, String otp) {
        // 1️⃣ Mail properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // 2️⃣ Session with authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // 3️⃣ Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );
            message.setSubject("Your OTP for Password Reset");
            message.setText("Your OTP is: " + otp + "\n\nIt will expire in 5 minutes.");

            // 4️⃣ Send email
            Transport.send(message);
            System.out.println("OTP sent to: " + recipientEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Service
    public static class JwtService {

        // Use the same secret key that you used while generating JWT
        private final String secret = "mysecretkeymysecretkeymysecretkey";

        // Extract userId from JWT token
        public String extractUserId(String token) {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            // Assuming you stored userId in "sub" or "userId" claim
            return claims.getSubject(); // or (String) claims.get("userId");
        }
    }
}
