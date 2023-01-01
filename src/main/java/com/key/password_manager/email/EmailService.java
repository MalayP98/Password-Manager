package com.key.password_manager.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String SENDER;

	/* Sends a simple mail. */
	public void sendMail(Email email) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(SENDER);
		mailMessage.setTo(email.getRecipient());
		mailMessage.setText(email.getMessage());
		mailMessage.setSubject(email.getSubject());
		javaMailSender.send(mailMessage);
	}

	/* Sends a HTML mail. */
	public void sendHTMLMail(Email email) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		helper.setText(email.getMessage(), true); // Use this or above line.
		helper.setTo(email.getRecipient());
		helper.setSubject(email.getSubject());
		helper.setFrom(SENDER);
		javaMailSender.send(mimeMessage);
	}
}
