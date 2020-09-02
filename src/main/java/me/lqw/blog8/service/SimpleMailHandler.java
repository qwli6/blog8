package me.lqw.blog8.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件处理器
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.2
 */
@Component
public class SimpleMailHandler {

    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 邮件 sender
     */
    private final JavaMailSender javaMailSender;

    public SimpleMailHandler(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * 发送邮件
     *
     * @throws MessagingException MessagingException
     */
    public void sendEmail() throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("22879754@qq.com");
        helper.setTo("liqiwen@lppz.com");
        helper.setSubject("主题：模板邮件");

        javaMailSender.send(mimeMessage);
    }
}
