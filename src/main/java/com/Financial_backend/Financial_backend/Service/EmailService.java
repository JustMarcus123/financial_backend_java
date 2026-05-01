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

    public void sendEmployeeInviteEmail(
            String toEmail,
            String firstName,
            String companyName,
            String enrollmentCode,
            String tempPassword) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("You're invited to join " + companyName
                    + "'s 401(k) Plan on EmpowerTrack");
            helper.setText(buildInviteEmailBody(
                    firstName, companyName,
                    toEmail, enrollmentCode,
                    tempPassword), true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to send invite email: " + e.getMessage());
        }
    }

    private String buildInviteEmailBody(
            String firstName,
            String companyName,
            String email,
            String enrollmentCode,
            String tempPassword) {

        String loginUrl = baseUrl + "/login";

        return """
        <div style="font-family:Arial,sans-serif;max-width:600px;
                    margin:0 auto;padding:32px;">

            <div style="background:#0C1E3C;padding:24px;
                        border-radius:8px 8px 0 0;text-align:center;">
                <h1 style="color:#fff;margin:0;">EmpowerTrack</h1>
                <p style="color:#5B8FF5;margin:4px 0 0;">
                    401(k) Retirement Platform
                </p>
            </div>

            <div style="background:#f9f9f9;padding:32px;
                        border:1px solid #e0e0e0;">

                <h2 style="color:#0C1E3C;">Hi %s 👋</h2>

                <p style="color:#333;line-height:1.6;">
                    <strong>%s</strong> has enrolled you in their 
                    401(k) retirement plan. Start saving for your 
                    future today — your employer will match your 
                    contributions!
                </p>

                <!-- Enrollment Code Box -->
                <div style="background:#F0FDF4;border:1px solid #BBF7D0;
                            border-radius:8px;padding:20px;margin:20px 0;">
                    <p style="margin:0 0 6px;color:#15803D;font-weight:bold;">
                        Your Enrollment Code
                    </p>
                    <p style="font-family:monospace;font-size:22px;
                              font-weight:bold;color:#166534;
                              background:#DCFCE7;padding:8px 16px;
                              border-radius:6px;display:inline-block;
                              letter-spacing:2px;margin:0;">
                        %s
                    </p>
                    <p style="color:#166534;font-size:12px;margin:8px 0 0;">
                        Use this code when setting up your account
                    </p>
                </div>

                <!-- Login Credentials -->
                <div style="background:#EFF6FF;border:1px solid #BFDBFE;
                            border-radius:8px;padding:20px;margin:20px 0;">
                    <p style="margin:0 0 8px;color:#1D4ED8;font-weight:bold;">
                        Your Login Credentials
                    </p>
                    <p style="margin:4px 0;color:#333;">
                        <strong>Email:</strong> %s
                    </p>
                    <p style="margin:4px 0;color:#333;">
                        <strong>Temporary Password:</strong>
                        <code style="background:#DBEAFE;padding:2px 8px;
                                     border-radius:4px;font-size:16px;">
                            %s
                        </code>
                    </p>
                </div>

                <div style="background:#FEF3C7;border:1px solid #FDE68A;
                            border-radius:8px;padding:14px;margin-bottom:24px;">
                    <p style="margin:0;color:#92400E;font-size:13px;">
                        ⚠️ You will be asked to change this password 
                        on first login. Please keep this email safe.
                    </p>
                </div>

                <div style="text-align:center;">
                    <a href="%s"
                       style="background:#0C1E3C;color:#fff;
                              padding:14px 32px;border-radius:8px;
                              text-decoration:none;font-weight:bold;
                              font-size:16px;display:inline-block;">
                        Join Your 401(k) Plan →
                    </a>
                </div>

            </div>

            <div style="background:#F3F4F6;padding:16px;text-align:center;
                        border-radius:0 0 8px 8px;">
                <p style="color:#9CA3AF;font-size:12px;margin:0;">
                    EmpowerTrack · Your retirement, secured.
                </p>
            </div>

        </div>
        """.formatted(
                firstName, companyName,
                enrollmentCode, email,
                tempPassword, loginUrl);
    }

}