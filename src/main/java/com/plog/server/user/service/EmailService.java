package com.plog.server.user.service;

import com.plog.server.user.config.EmailConfig;
import com.plog.server.user.domain.EmailToken;
import com.plog.server.user.domain.UserTemp;
import com.plog.server.user.repository.EmailTokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EmailService {
    private static final String CONFIRM_EMAIL_PATH = "/user/confirm-email";
    private final JavaMailSender javaMailSender;
    private final EmailTokenRepository emailTokenRepository;
    private final EmailConfig emailConfig;

    @Async
    public void sendEmail(MimeMessage mimeMessage) {
        javaMailSender.send(mimeMessage);
    }

    public Map<String, Object> createEmailToken(UserTemp userTemp) throws MessagingException {
        EmailToken emailToken = EmailToken.createEmailToken(userTemp);
        emailTokenRepository.save(emailToken);

        MimeMessage mimeMessage = createVerifyLink(userTemp, emailToken);

        sendEmail(mimeMessage);

        Map<String,Object> response = new HashMap<>();
        response.put("uuid", emailToken.getEmailUuid());
        response.put("account", userTemp.getTempAccount());

        return response;
    }

    public  MimeMessage createVerifyLink(UserTemp userTemp, EmailToken emailToken)throws MessagingException {
        // 이메일 전송을 위한 MimeMessage 생성 및 설정
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,"UTF-8");
        helper.setTo(userTemp.getTempEmail());
        helper.setSubject("플log 회원가입 이메일 인증");
        helper.setFrom("nkdy50315031@gmail.com");

        String emailContent
                = "<a href='" + emailConfig.getBaseUrl() + CONFIRM_EMAIL_PATH + "?uuid=" + emailToken.getEmailUuid() + "'>플log 이메일 인증</a>";
        helper.setText(emailContent, true);
        return  mimeMessage;
    }

    //uuid로 usertemp 조히
    public  UserTemp findByUuid(String uuid){
        return emailTokenRepository.findByEmailUuid(uuid)
                .map(EmailToken::getUserTemp)
                .orElseThrow(()->new IllegalArgumentException("uuid에 해당하는 usertemp조회 실패"+ uuid));
    }
}
