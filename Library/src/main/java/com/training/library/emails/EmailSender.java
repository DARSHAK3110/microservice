package com.training.library.emails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

	@Autowired
	private JavaMailSender emailSender;

	public void sendSimpleMessage(String to, String subject, String normalText){

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("managementlibrary9@gmail.com");
		message.setTo(to);
		message.setText(normalText);
		message.setSubject(subject);
		emailSender.send(message);
	}
}
