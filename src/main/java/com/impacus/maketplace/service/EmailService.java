package com.impacus.maketplace.service;

import com.impacus.maketplace.common.constants.BaseConstants;
import com.impacus.maketplace.common.enumType.MailType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.EmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final ObjectCopyHelper objectCopyHelper;

    /**
     * 단일 이메일 전송 함수
     */
    public void sendSingleEmail(
            EmailDTO dto
    ) {
        try {
            // 이메일 전송을 위한 MimeMessageHelper 객체 생성
            MimeMessageHelper msgHelper = prepareMessage(dto.getMailType(), dto.getEmailContent());
            msgHelper.setSubject(dto.getSubject());
            msgHelper.setTo(dto.getReceiveEmail());

            // 이메일 전송
            javaMailSender.send(msgHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new CustomException(CommonErrorType.FAIL_TO_SEND_EMAIL);
        }
    }

    private MimeMessageHelper prepareMessage(
            String mailType,
            Map<String, String> emailContent
    ) throws MessagingException {
        // 이메일 전송을 위한 MimeMessageHelper 객체 생성
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(
                mimeMessage,
                false,
                BaseConstants.ENCODING_UTF_8
        );

        // 템플릿에 매핑된 값을 설정
        Context context = new Context(LocaleContextHolder.getLocale());
        emailContent.forEach(context::setVariable);

        // 템플릿을 처리하여 이메일 본문 생성
        String emailBody = templateEngine.process(mailType, context);
        msgHelper.setText(emailBody, true);
        return msgHelper;
    }

    private String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0:
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                default:
                    key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

    private String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }

    /**
     * 이메일 인증 메일을 보내는 함수
     *
     * @param receiver
     * @return
     */
    public String sendEmailVerificationMail(String receiver, UserType role) {
        MailType mailType = switch (role) {
            case ROLE_CERTIFIED_USER -> MailType.EMAIL_VERIFICATION;
            case ROLE_APPROVED_SELLER -> MailType.SELLER_EMAIL_VERIFICATION;
            default -> MailType.AUTH;
        };
        String authNumber = createCode();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper msgHelper = new MimeMessageHelper(
                    mimeMessage,
                    false,
                    BaseConstants.ENCODING_UTF_8
            );
            msgHelper.setTo(receiver);
            msgHelper.setSubject(mailType.getSubject());
            msgHelper.setText(setContext(authNumber, mailType.getTemplate()), true);
            javaMailSender.send(mimeMessage);

            return authNumber;
        } catch (MessagingException e) {
            throw new CustomException(CommonErrorType.FAIL_TO_SEND_EMAIL);
        }
    }

    @Transactional
    public void sendAlarmMail(EmailDTO emailDto, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            msgHelper.setTo(emailDto.getReceiveEmail());
            msgHelper.setFrom("support@implace.com");
            msgHelper.setSubject(emailDto.getSubject());
            msgHelper.setText(text, true);
            javaMailSender.send(mimeMessage);

            emailDto.setReceiveEmail(Base64.getEncoder().
                    encodeToString(emailDto.getReceiveEmail().getBytes(StandardCharsets.UTF_8)));
            emailDto.setAuthNo("");
            emailDto.setMailType("17");

        } catch (MessagingException e) {
            throw new CustomException(e);
        }
    }

    @Transactional
    public Boolean sendMail(EmailDTO emailDto, MailType mailType) {
        String authNumber = createCode();
        if (!mailType.equals(MailType.AUTH)) {
            authNumber = "";
        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            msgHelper.setTo(emailDto.getReceiveEmail());
            msgHelper.setFrom("support@implace.com");
            msgHelper.setSubject(mailType.getSubject());
            msgHelper.setText(setContext(authNumber, mailType.getTemplate()), true);
            javaMailSender.send(mimeMessage);

            emailDto.setReceiveEmail(Base64.getEncoder().
                    encodeToString(emailDto.getReceiveEmail().getBytes(StandardCharsets.UTF_8)));
            emailDto.setAuthNo(authNumber);
            emailDto.setMailType(mailType.getCode());

            return true;
        } catch (MessagingException e) {
            throw new CustomException(e);
        }
    }
}
