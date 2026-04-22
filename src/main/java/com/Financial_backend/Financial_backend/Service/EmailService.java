package com.Financial_backend.Financial_backend.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    public void sendEmployerWelcomeEmail(
            String toEmail,
            String contactName,
            String companyName,
            String tempPassword) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false); // false = plain text

            helper.setTo(toEmail);
            helper.setSubject("Welcome to EmpowerTrack — Your Login Credentials");
            helper.setText(buildEmailBody(contactName, companyName, toEmail, tempPassword));

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send welcome email: " + e.getMessage());
        }
    }

    private String buildEmailBody(String name, String companyName,
                                  String email, String tempPassword) {
        return """
        Welcome, %s!

        Your company plan for %s has been activated on EmpowerTrack.

        Your login credentials:
            Email:              %s
            Temporary Password: %s

        Please log in and change your password immediately.

        If you have any questions, contact your plan administrator.

        — EmpowerTrack Team
        """.formatted(name, companyName, email, tempPassword);
    }
}