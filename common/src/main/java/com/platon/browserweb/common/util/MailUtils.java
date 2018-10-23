package com.platon.browserweb.common.util;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.util.List;


public class MailUtils {

    /**
     * 发送 邮件方法 (Html格式，支持附件)
     *
     * @return void
     */
    public static boolean sendEmail(MailInfo mailInfo) {
        //邮箱
        String mailSmtpHost = PropertyConfigurer.getStringValue("mail.smtp.host").trim();
        String mailFrom = PropertyConfigurer.getStringValue("mail.from").trim();
        String mailUser = PropertyConfigurer.getStringValue("mail.smtp.user").trim();
        String mailPassword = PropertyConfigurer.getStringValue("mail.smtp.password").trim();

        try {
            HtmlEmail email = new HtmlEmail();
            // 配置信息
            email.setHostName(mailSmtpHost);
            email.setFrom(mailFrom);
            email.setAuthentication(mailUser,mailPassword);
            email.setCharset("UTF-8");
            email.setSubject(mailInfo.getSubject());
            email.setHtmlMsg(mailInfo.getContent());

            // 添加附件
            List<EmailAttachment> attachments = mailInfo.getAttachments();
            if (null != attachments && attachments.size() > 0) {
                for (int i = 0; i < attachments.size(); i++) {
                    email.attach(attachments.get(i));
                }
            }

            // 收件人
            List<String> toAddress = mailInfo.getToAddress();
            if (null != toAddress && toAddress.size() > 0) {
                for (int i = 0; i < toAddress.size(); i++) {
                    email.addTo(toAddress.get(i));
                }
            }
            // 抄送人
            List<String> ccAddress = mailInfo.getCcAddress();
            if (null != ccAddress && ccAddress.size() > 0) {
                for (int i = 0; i < ccAddress.size(); i++) {
                    email.addCc(ccAddress.get(i));
                }
            }
            //邮件模板 密送人
            List<String> bccAddress = mailInfo.getBccAddress();
            if (null != bccAddress && bccAddress.size() > 0) {
                for (int i = 0; i < bccAddress.size(); i++) {
                    email.addBcc(ccAddress.get(i));
                }
            }
            email.send();
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }

    }
}
