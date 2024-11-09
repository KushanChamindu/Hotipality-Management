package com.example.megablissmainservice.utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;

import com.example.megablissmainservice.Config.AuthenticationConstants;
import com.example.megablissmainservice.Entity.RoleType;
import com.example.megablissmainservice.exception.UserServiceCustomException;

@Component
@Slf4j
public class EmailUtility {
    @Value("${super-user.email}")
    private String superUserEmail;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Retryable(retryFor = {
            Exception.class }, maxAttemptsExpression = "${retry.config.maxAttempts:3}", backoff = @Backoff(delayExpression = "${retry.config.backoffDelay:3000}"))
    public void sendVerificationEmail(String to, String username, Long verificationCode, Set<RoleType> roles)
            throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        if (AuthenticationConstants.ADMINTATION_ROLES.stream().anyMatch(roles::contains)) {
            helper.setTo(superUserEmail);
            helper.setSubject("Activate  " + username + "'s Account");
        } else {
            helper.setTo(to);
            helper.setSubject("Activate Your Account");
        }
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("verificationCode", verificationCode);
        String html = templateEngine.process("accountValidateEmailTemplate", context);
        helper.setText(html, true);
        javaMailSender.send(message);
    }

    @Retryable(retryFor = {
            Exception.class }, maxAttemptsExpression = "${retry.config.maxAttempts:3}", backoff = @Backoff(delayExpression = "${retry.config.backoffDelay:3000}"))
    public void sendForgetPasswordEmail(String to, String username, Long verificationCode) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(to);
        helper.setSubject("Forgot Password");
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("verificationCode", verificationCode);
        String html = templateEngine.process("forgotPasswordEmailTemplate", context);
        helper.setText(html, true);
        javaMailSender.send(message);
    }

    @Recover
    public void recoverMovieDetails(Exception e, String to) throws UserServiceCustomException {
        // Log the error
        log.error("Unable to send email to: " + to);

        throw new UserServiceCustomException(
                "Unable to send email to: " + to + ". Error cause : " + e.getMessage(),
                "UNABLE_SEND_EMAIL", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
