package hud.app.event_management.serviceImpl;

import hud.app.event_management.model.EmailType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String from;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine engine;

    @Async
    public void sendEmail(String to, EmailType emailType, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(emailType.getSubject());

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", from);
        properties.put("activation_code", token);

        Context context = new Context();
        context.setVariables(properties);

        String body = engine.process(emailType.name().toLowerCase(), context);
        helper.setText(body, true);
        javaMailSender.send(message);
    }
}