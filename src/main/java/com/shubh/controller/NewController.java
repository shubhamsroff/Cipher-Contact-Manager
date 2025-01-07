package com.shubh.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shubh.entities.Email;
import com.shubh.entities.User;
import com.shubh.repository.UserRepository;
import com.shubh.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class NewController {

	Random random = new Random(10000);
	@Autowired
	private UserRepository userRepository;
	private int passwordOtp = 0;
	@Autowired
	private EmailService emailService;

	@GetMapping("/advance")
	public String index() {
		return "Normal/index";
	}

	@PostMapping("/sendEmail")
	public String sendEmail(@ModelAttribute Email email, HttpSession session) {
		int otp = random.nextInt(9999999);
		passwordOtp = otp;
		String message = "<div style='border:1px solid #e2e2e2;padding:20px'>"
				+ "<h1>"
				+ "OTP is"
				+ "<b>" + otp + "</b>"
				+ "</h1>"
				+ "</div>";

		email.setMessage(email.getMessage() + message);
		boolean flag = emailService.sendMail(email);
		if (flag) {
			session.setAttribute("msg", "Email Send Successfully");
			session.setAttribute("otp2", otp);
			session.setAttribute("email", email.getSubject());
			return "verify_otp";
		}

		System.out.println(email + " Email is ");
		return "redirect:/advance";
	}

	@PostMapping("/send-otp-password")
	public String verifyOtp(@RequestParam("otp") String otp, HttpSession session) {
		int s = (Integer) session.getAttribute("otp2");
		String email = (String) session.getAttribute("email");
		if (s == Integer.parseInt(otp)) {

			User user = this.userRepository.getUserByUserName(email);
			if (user == null) {
				session.setAttribute("message", "No User Does not Exist");

				return "/advance";
				// Send Error

			} else
				return "password_change_form";
		} else {
			session.setAttribute("message", "You Entered Wrong Otp !!");
			return "verify_otp";
		}
	}

}
