package com.example.mega.utils;

import com.example.mega.db.domain.TaskEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendTask(String to, TaskEntity task) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Task Created: " + task.getTitle());
            helper.setText("Task details:\n\n" + "Title: " + task.getTitle() + "\nDescription: " + task.getDescription());

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
        }
    }
}
