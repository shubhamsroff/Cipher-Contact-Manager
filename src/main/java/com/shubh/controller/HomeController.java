package com.shubh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.shubh.entities.User;
import com.shubh.helper.Message;
import com.shubh.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;

	public HomeController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	// @GetMapping("/")
	// public String homePage(Model m)
	// {
	// m.addAttribute("title","Home-Smart-Contact-Manager");
	// return "base";
	// }
	@GetMapping("/")
	public String homePage1(Model m) {
		m.addAttribute("title", "Home-Smart-Contact-Manager");
		return "home";
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/signup")
	public String signUp(Model m) {
		m.addAttribute("title", "Register-Smart Contact Manager");
		m.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult res, Model model,
			HttpSession session, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement) {
		try {
			if (!agreement) {

				throw new Exception("You have not agreed the terms and conditions:");
			}
			if (res.hasErrors()) {
				model.addAttribute("user", user);
				System.out.println("ERROR" + res.toString());
				return "signup";
			}

			user.setActive(true);
			user.setImageUrl("/shubh.jpg");
			user.setRole("ROLE_USER");

			User result = this.userRepository.save(user);
			System.out.println(result + " ");
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered!!", "alert-success"));
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();

			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong" + e.getMessage(), "alert-danger"));
			return "signup";
		}
	}

}
