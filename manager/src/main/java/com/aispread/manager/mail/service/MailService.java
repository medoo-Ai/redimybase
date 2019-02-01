package com.aispread.manager.mail.service;

/**
 * @auther SyntacticSugar
 * @data 2019/2/1 0001下午 3:39
 */
public interface MailService {
    void sendSimpleMail(String to, String subject, String content);
}
