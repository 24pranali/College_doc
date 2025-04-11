package com.example.demo_pranali.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import javax.mail.internet.MimeMessage;
//import org.springframework.web.multipart.MultipartFile;

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
//    public void sendLorRequestEmailWithAttachment(String toEmail, String studentName, MultipartFile resumeFile) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);  // true = multipart
//
//            helper.setFrom(fromEmail);
//            helper.setTo(toEmail);
//            helper.setSubject("LOR Request from Student");
//
//            String body = "Dear Professor,\n\nStudent " + studentName + " has requested a Letter of Recommendation. The resume is attached.\n\nRegards,\nPortal Team";
//
//            helper.setText(body);
//
//            // Attach Resume
//            helper.addAttachment(resumeFile.getOriginalFilename(), resumeFile);
//
//            mailSender.send(message);
//        } catch (Exception e) {
//            System.err.println("❌ Failed to send email with attachment: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    public void sendRequestStatusEmail(String toEmail, String studentName, String documentType, boolean approved) {
        String subject = approved
                ? "✅ Your document request has been approved"
                : "❌ Your document request has been rejected";

        String message = approved
                ? "Dear " + studentName + ",\n\nYour request for \"" + documentType + "\" has been approved. You may now download the document from the portal.\n\nRegards,\nAdmin Team"
                : "Dear " + studentName + ",\n\nUnfortunately, your request for \"" + documentType + "\" has been rejected.\nPlease log in to view the reason or contact the admin.\n\nRegards,\nAdmin Team";

        sendEmail(toEmail, subject, message);  // Reuse existing method
    }
    public void sendLorRequestEmail(String toEmail, String studentName) {
        String subject = "LOR Request from Student";
        String message = "Dear Professor,\n\nStudent " + studentName + " has requested a Letter of Recommendation. The resume is attached.\n\nRegards,\nPortal Team";
        sendEmail(toEmail, subject, message);
    }

    // For sending OTP during account creation
    public void sendOtpEmail(String toEmail, String otp)
    {
        String subject = "📌 OTP for College Portal Account Verification";
        String message = "Dear Student,\n\n" +
                "Your One-Time Password (OTP) for account verification is: " + otp + "\n\n" +
                "This OTP is valid for 10 minutes only.\n\n" +
                "Regards,\nCollege Admin Team";

        sendEmail(toEmail, subject, message);
    }

}
