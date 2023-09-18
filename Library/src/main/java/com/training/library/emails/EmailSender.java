package com.training.library.emails;

import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

	@Autowired
	private JavaMailSender emailSender;

	public void sendSimpleMessage(String to, String subject, String normalText, Boolean reservation)
			throws MessagingException, IOException {
		MimeMessage helper = emailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(helper, true);

		MimeMultipart content = new MimeMultipart("related");
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setText("<html>" + "<body>" + normalText + "</body>" + "</html>", "US-ASCII", "html");
		content.addBodyPart(textPart);
		
		if (!reservation) {
			MimeBodyPart imagePart = new MimeBodyPart();
			imagePart = new MimeBodyPart();
			DataSource fds = new FileDataSource(new ClassPathResource("static/bookback.jpeg").getFile());
			imagePart.setDataHandler(new DataHandler(fds));
			imagePart.setFileName("Gift voucher");
			content.addBodyPart(imagePart);
		}
		helper.setContent(content);
		message.setFrom("managementlibrary9@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		emailSender.send(helper);
	}
}
