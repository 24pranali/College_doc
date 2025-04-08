package com.example.demo_pranali.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail; // Sender Email from application.properties

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
      try {
          SimpleMailMessage message = new SimpleMailMessage();
          message.setFrom(fromEmail);
          message.setTo(to);
          message.setSubject(subject);
          message.setText(text);
          mailSender.send(message);
      } catch (Exception e) {
          System.err.println("❌ Failed to send email: " + e.getMessage());
          e.printStackTrace();
      }
    }
    public void sendRequestStatusEmail(String toEmail, String studentName, String documentType, boolean approved) {
        String subject = approved
                ? "✅ Your document request has been approved"
                : "❌ Your document request has been rejected";

        String message = approved
                ? "Dear " + studentName + ",\n\nYour request for \"" + documentType + "\" has been approved. You may now download the document from the portal.\n\nRegards,\nAdmin Team"
                : "Dear " + studentName + ",\n\nUnfortunately, your request for \"" + documentType + "\" has been rejected.\nPlease log in to view the reason or contact the admin.\n\nRegards,\nAdmin Team";

        sendEmail(toEmail, subject, message);  // Reuse existing method
    }

}
