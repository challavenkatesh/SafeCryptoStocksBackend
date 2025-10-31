package com.safecryptostocks.service;

import com.safecryptostocks.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTradeNotification(User user, String tradeType, String symbol, String amount, String price, String total) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Trade " + tradeType + " Confirmation - SafeCryptoStocks");

            String content = "<div style='font-family: Arial, sans-serif; padding: 15px;'>"
                    + "<h2 style='color: #007BFF;'>Hello, " + user.getFullname() + " 👋</h2>"
                    + "<p>Your trade has been successfully processed.</p>"
                    + "<p><b>Trade Type:</b> " + tradeType + "</p>"
                    + "<p><b>Asset Symbol:</b> " + symbol + "</p>"
                    + "<p><b>Amount:</b> " + amount + "</p>"
                    + "<p><b>Price:</b> " + price + "</p>"
                    + "<p><b>Total Value:</b> " + total + "</p>"
                    + "<hr>"
                    + "<p>Thank you for trading with <b>SafeCryptoStocks</b> 🚀</p>"
                    + "</div>";

            helper.setText(content, true);
            mailSender.send(message);

            System.out.println("✅ Trade email sent successfully to " + user.getEmail());

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send trade notification email: " + e.getMessage());
        }
    }
}
