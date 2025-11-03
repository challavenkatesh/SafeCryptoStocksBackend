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
    private String senderEmail; // Sender email from application.properties

    @Value("${spring.mail.password}")
    private String senderPassword;

    // ============================
    // 🔹 OTP MAIL SERVICE
    // ============================
    public void sendOtp(String recipientEmail, String otp) {
        String subject = "Your OTP for Password Reset";
        String body = "Your OTP is: " + otp + "\n\nIt will expire in 5 minutes.";
        sendMail(recipientEmail, subject, body);
    }

    // ============================
    // 🔹 TRADE NOTIFICATION (BUY / SELL)
    // ============================
    public void sendTradeNotification(
            String recipientEmail,
            String fullName,
            String tradeType,
            double amount,
            String currency
    ) {
        String subject = "SafeCryptoStocks - " + tradeType + " Transaction Successful";
        String body = String.format(
                "Hello %s,\n\nYour %s transaction was successful.\n\nDetails:\n- Type: %s\n- Amount: %.2f %s\n\nThank you for using SafeCryptoStocks!\n\n— Team SafeCryptoStocks",
                fullName, tradeType.toLowerCase(), tradeType.toUpperCase(), amount, currency
        );
        sendMail(recipientEmail, subject, body);
    }

    // ============================
    // 🔹 WALLET NOTIFICATION
    // ============================
    public void sendWalletNotification(
            String recipientEmail,
            String fullName,
            double amount,
            String currency,
            String type
    ) {
        String subject = "SafeCryptoStocks - Wallet " + type + " Successful";
        String body = String.format(
                "Hello %s,\n\nYour wallet has been successfully %s.\n\nDetails:\n- Type: %s\n- Amount: %.2f %s\n\nYou can check your wallet balance anytime in your SafeCryptoStocks account.\n\n— Team SafeCryptoStocks",
                fullName,
                type.equalsIgnoreCase("DEPOSIT") ? "credited" : "debited",
                type,
                amount,
                currency
        );
        sendMail(recipientEmail, subject, body);
    }

    // ============================
    // 🔹 COMMON MAIL SENDER
    // ============================
    private void sendMail(String recipientEmail, String subject, String body) {
        // Mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Authenticator for login
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create the message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            // Send mail
            Transport.send(message);
            System.out.println("✅ Email sent successfully to: " + recipientEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email to: " + recipientEmail);
            e.printStackTrace();
        }
    }

    // ============================
    // 🔹 JWT SERVICE (Extract userId)
    // ============================
    @Service
    public static class JwtService {
        // Use the same secret key used while generating the JWT token
        private final String secret = "mysecretkeymysecretkeymysecretkey";

        public String extractUserId(String token) {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // or (String) claims.get("userId");
        }
    }
}
