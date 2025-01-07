package com.shubh.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shubh.entities.Email;

import jakarta.mail.internet.MimeMessage;





@Controller
public class ForgotController {

	
	@Autowired
	private JavaMailSender javaMailSender;
	Random random=new Random(1000);

	public void sendMail(Email email)
	{
		try {
			MimeMessage message=javaMailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(message);
			helper.setFrom("shubhamsroff322@gmail.com");
			helper.setTo(email.getTo());
			helper.setSubject(email.getSubject());
			helper.setText(email.getMessage());
			 javaMailSender.send(message);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
//	Email Id Form Open Handle
	@RequestMapping("/forgot")
	public String openEmailForm()
	{
		return "forgot_email_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email)
	{
		System.out.println(email+" EMAIL");
//		Generating Otp of 4 digit
		int otp = random.nextInt(999999);
		String subject="OTPFromSCM";
		String message="<h1>OTP="+otp+"</h1>";
		String to=email;
		System.out.println(otp+" "+subject+" "+to);
		Email em=new Email(subject,to,message);
		sendMail(em);
		
//		Write code to send otp on email
		return "verify_otp";
	}
}
