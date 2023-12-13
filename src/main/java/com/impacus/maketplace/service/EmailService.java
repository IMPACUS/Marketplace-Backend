package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.MailType;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.EmailDto;
import com.impacus.maketplace.entity.common.EmailHistory;
import com.impacus.maketplace.repository.EmailHistoryRepository;
import com.impacus.maketplace.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MailDateFormat;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailHistoryRepository emailHistoryRepository;
    private final ObjectCopyHelper objectCopyHelper;

    public Boolean sendMail(EmailDto emailDto,MailType mailType) {
        String authNumber = createCode();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            msgHelper.setTo(emailDto.getReceiveEmail());
            msgHelper.setSubject(emailDto.getSubject());
            msgHelper.setText(setContext(authNumber, mailType.getTemplate()), true);
            javaMailSender.send(mimeMessage);

            emailDto.setReceiveEmail(Base64.getEncoder().
                    encodeToString(emailDto.getReceiveEmail().getBytes(StandardCharsets.UTF_8)));
            emailDto.setAuthNo(authNumber);
            emailDto.setMailType(mailType.getCode());
            EmailHistory emailHistory = objectCopyHelper.copyObject(emailDto, EmailHistory.class);
            EmailHistory result = emailHistoryRepository.save(emailHistory);

            return result != null ? true : false;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean checkAuthNumber(EmailDto emailDto) {

        emailDto.setReceiveEmail(Base64.getEncoder().encodeToString(emailDto.getReceiveEmail().getBytes(StandardCharsets.UTF_8)));

        // 1 안
//        String authNumber = emailHistoryRepository.findAuthNoByReceiveEmailAndAuthNoAndSendDatetime(emailDto.getReceiveEmail(), emailDto.getAuthNo()).stream().findFirst().get();
        // 2 안

        LocalDateTime threeMinutesAgoTime = LocalDateTime.now().minusMinutes(3);
        String authNumber = emailHistoryRepository.findByReceiveEmailAndAuthNoAndSendDatetimeGreaterThan(emailDto.getReceiveEmail(), emailDto.getAuthNo(), threeMinutesAgoTime).stream().findFirst().get();
        return authNumber == null ? false : true;
    }

    private String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

    private String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }





}
