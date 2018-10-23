package com.platon.browserweb.common.thread;



import com.platon.browserweb.common.util.MailInfo;
import com.platon.browserweb.common.util.MailUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/3/30
 * Time: 15:54
 */
public class SendMailThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(SendMailThread.class);

    private List<String> toList;

    private String content;

    private String subject;

    public SendMailThread(String content, String subject, String... emails) {
        this.content = content;
        this.subject = subject;
        this.toList = Arrays.asList(emails);
    }

    public void run() {
        MailInfo mailInfo = new MailInfo();
        mailInfo.setSubject(subject);
        mailInfo.setContent(content);
        if (CollectionUtils.isNotEmpty(toList)) {
            mailInfo.setToAddress(toList);
            MailUtils.sendEmail(mailInfo);
        }
        logger.info("邮件发送成功");
    }
}