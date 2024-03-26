package org.cs407team7.KitchenCompanion.service;

import jakarta.mail.internet.MimeMessage;
import org.cs407team7.KitchenCompanion.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(User user, String verifyCode, long id) throws MailException {
        String url = "https://kitchencompanion.eastus.cloudapp.azure.com/api/v1";
        // TODO make sure links are generalised
        sendEmail(user.getEmail(), "Verify Email", "Please click link to verify: " + "\r\n" + url + "/user/verify?uid=" + id + "&token=" + verifyCode);
    }

    public void sendEmail(String email, String subject, String contents) {
        SimpleMailMessage mail = new SimpleMailMessage();
        // TODO: get from config instead of hard coding
        mail.setFrom("kitchencompanion.noreply@gmail.com");
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(contents);
        mailSender.send(mail);
    }

    public void sendEmailHtml(String email, String subject, String contents) {
        // Set up generic here
        String header = "<div style=\"max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">\n" +
                "<h1 style=\"text-align: center; color: #2D3566;\">Kitchen Companion</h1>\n";
        String footer = "</div>";
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("curio.noreply@gmail.com");
            messageHelper.setTo(email);
            messageHelper.setSubject(subject);
            messageHelper.setText(header + contents + footer, true);
            mailSender.send(mimeMessage);
        } catch (Exception ex) {
            System.out.println("Failed to send rich email");
            sendEmail(email, subject, contents);
        }
    }

}
