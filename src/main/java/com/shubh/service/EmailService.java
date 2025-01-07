package com.shubh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.shubh.entities.Email;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	public boolean sendMail(Email email)
	{
		try {
			MimeMessage message=javaMailSender.createMimeMessage();
			message.setContent(message,"text/html");
			MimeMessageHelper helper=new MimeMessageHelper(message);
			
			helper.setFrom("shubhamsroff322@gmail.com");
			helper.setTo(email.getTo());
			helper.setSubject(email.getSubject());
			helper.setText(email.getMessage());
			 javaMailSender.send(message);
			 return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
