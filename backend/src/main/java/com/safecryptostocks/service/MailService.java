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
    private String senderEmail;

    @Value("${spring.mail.password}")
    private String senderPassword;

    // ============================
    // 🔹 OTP MAIL SERVICE
    // ============================
    public void sendOtp(String recipientEmail, String otp) {
        String subject = "SafeCryptoStocks - Your One-Time Password (OTP)";
        String body = """
                <div style='font-family: Arial, sans-serif;'>
                    <h2>🔐 Your OTP Verification Code</h2>
                    <p>Hello User,</p>
                    <p>Your OTP is: <strong style='color:#0b93f6; font-size:18px;'>%s</strong></p>
                    <p>This OTP will expire in <b>5 minutes</b>.</p>
                    <br>
                    <p>Regards,<br><b>Team SafeCryptoStocks</b></p>
                </div>
                """.formatted(otp);

        sendMail(recipientEmail, subject, body, true);
    }

    // ============================
    // 🔹 TRADE NOTIFICATION
    // ============================
    public void sendTradeNotification(String recipientEmail, String fullName, String tradeType, double amount, String currency) {
        String subject = "SafeCryptoStocks - " + tradeType + " Transaction Successful";
        String body = """
                <div style='font-family: Arial, sans-serif;'>
                    <h2>💹 Trade Confirmation</h2>
                    <p>Hello %s,</p>
                    <p>Your %s transaction was successful.</p>
                    <ul>
                        <li><b>Type:</b> %s</li>
                        <li><b>Amount:</b> %.2f %s</li>
                    </ul>
                    <p>Thank you for trading with <b>SafeCryptoStocks</b>!</p>
                    <p>— Team SafeCryptoStocks</p>
                </div>
                """.formatted(fullName, tradeType.toLowerCase(), tradeType.toUpperCase(), amount, currency);

        sendMail(recipientEmail, subject, body, true);
    }

    // ============================
    // 🔹 WALLET NOTIFICATION
    // ============================
    public void sendWalletNotification(String recipientEmail, String fullName, double amount, String currency, String type) {
        String subject = "SafeCryptoStocks - Wallet " + type + " Successful";
        String body = """
                <div style='font-family: Arial, sans-serif;'>
                    <h2>💰 Wallet Update</h2>
                    <p>Hello %s,</p>
                    <p>Your wallet has been successfully %s.</p>
                    <ul>
                        <li><b>Type:</b> %s</li>
                        <li><b>Amount:</b> %.2f %s</li>
                    </ul>
                    <p>You can check your wallet balance anytime in your SafeCryptoStocks account.</p>
                    <p>— Team SafeCryptoStocks</p>
                </div>
                """.formatted(fullName, type.equalsIgnoreCase("DEPOSIT") ? "credited" : "debited", type, amount, currency);

        sendMail(recipientEmail, subject, body, true);
    }

    // ============================
    // 🔹 COMMON MAIL SENDER
    // ============================
    private void sendMail(String recipientEmail, String subject, String body, boolean isHtml) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);

            if (isHtml)
                message.setContent(body, "text/html; charset=utf-8");
            else
                message.setText(body);

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
        private final String secret = "mysecretkeymysecretkeymysecretkey";

        public String extractUserId(String token) {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        }
    }
}
