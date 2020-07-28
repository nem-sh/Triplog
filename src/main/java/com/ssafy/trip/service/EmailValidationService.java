package com.ssafy.trip.service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class EmailValidationService {
	private static final Logger logger = LoggerFactory.getLogger(EmailValidationService.class);
	
	@Autowired
    JavaMailSender javaMailSender;
	
	@Async
	public void sendEmail(String email) {
		MimeMessage simpleMessage = javaMailSender.createMimeMessage();
        String memberMail = email;
        try {
			simpleMessage.addRecipient(RecipientType.TO, new InternetAddress(memberMail));
			simpleMessage.setSubject("TRIP 회원가입 인증 메일");
			simpleMessage.setText(new StringBuffer().append("<h1>회원가입 인증메일입니다.</h1>")
					.append("<p>밑의 링크를 클릭하면 메일이 인증 됩니다.</p>")
					.append("<a href='http://localhost:8080/emailValid/auth/"+memberMail)
					.append("' target=blank>메일 인증 링크</a>")
					.toString(), "UTF-8", "html");
			javaMailSender.send(simpleMessage);
			logger.info("async send email to : " + email);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
