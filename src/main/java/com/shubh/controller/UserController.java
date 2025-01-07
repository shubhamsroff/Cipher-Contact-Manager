package com.shubh.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shubh.entities.Contact;
import com.shubh.entities.User;
import com.shubh.helper.Message;
import com.shubh.repository.ContactRepository;
import com.shubh.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Pageable;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequestMapping("/user")
@Controller
public class UserController {

	// @Autowired
	// private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;

	// Adding Common Data
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {

		// System.out.println(principal.getName());
		// System.out.println("Add common data ");
		User user = userRepository.getUserByUserName(principal.getName());
		if (user == null) {

		} else {
			model.addAttribute("user", user);
			model.addAttribute("userName", user.getName());
		}
	}

	// DashBoard Home
	@RequestMapping("/index")
	public String dashBoard(Principal principal) {
		if (userRepository.getUserByUserName(principal.getName()) != null)
			return "Normal/user_dashboard";
		else
			return "/newAdd";
	}

	// About Page
	@RequestMapping("/about")
	@ResponseBody
	public String about(Principal principal) {
		if (userRepository.getUserByUserName(principal.getName()) != null)
			return "Working on it";
		else
			return "/newAdd";
	}

	// Open add form-Handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "Normal/add_contact_form";
	}

	// Processing Add-Contact Form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute("contact") Contact contact, Principal principal,
			@RequestParam("profileImage") MultipartFile file, HttpSession session) throws IOException {
		String name = principal.getName();
		try {
			User user = this.userRepository.getUserByUserName(name);
			// Processing and Upload file
			if (file.isEmpty()) {
				// if the file is empty
				System.out.println("File is Empty:");
				contact.setImageUrl("contact.png");
			} else {
				contact.setImageUrl(file.getOriginalFilename());
				File newFile = new ClassPathResource("static/Images").getFile();
				Path path = Paths.get(newFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepository.save(user);
			System.out.println("Added Data is Database");
			// Success Message
			session.setAttribute("message", new Message("Your Contact is Added ! Add next..", "success"));
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something Went Wrong ! Try Again", "danger"));

		}
		return "Normal/add_contact_form";
	}
	// Show Contact Handler
	// Per page=5[n]
	// current page=0[page]

	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,
			Model m, Principal principal) {
		String userName = principal.getName();
		// User user=this.userRepository.getUserByUserName(userName);
		// List<Contact>useList=user.getContacts();
		//
		User user = this.userRepository.getUserByUserName(userName);
		int id = user.getId();
		Pageable pageable = PageRequest.of(page, 2);
		Page<Contact> contacts = this.contactRepository.findContactsByUser(id, pageable);
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());
		return "Normal/show_contacts";
	}

	// Showing Particular Contact Details
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer id, Model model, Principal principal) {
		System.out.println(id + " ram");
		Optional<Contact> contactById = this.contactRepository.findById(id);
		Contact contact = contactById.get();

		// Current User
		User user = this.userRepository.getUserByUserName(principal.getName());
		if (user.getId() == contact.getUser().getId())
			model.addAttribute("contact", contact);
		else
			System.out.println("Wrong choiice!");
		return "Normal/contact_details";
	}

	// Delete Contact Mapping
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer id, Model model, Principal principal,
			HttpSession session) {

		Optional<Contact> contactOptional = this.contactRepository.findById(id);
		Contact contact = contactOptional.get();
		User user = this.userRepository.getUserByUserName(principal.getName());

		// remove
		// img
		// contact.getImage()

		if (user.getId() == contact.getUser().getId()) {
			contact.setUser(null);
			session.setAttribute("mess", new Message("Successfully Deleted..!", "success"));

			// Removing Data From Database:-

			// this.contactRepository.deleteContactById(id);
			// Without Removing Data from Database:-
			this.contactRepository.deleteById(id);
		}

		return "redirect:/user/show-contacts/0";
	}

	// Updating Form
	// open update form handler
	@PostMapping("/update-contact/{cId}")
	public String updateForm(@PathVariable("cId") Integer id, Model m) {
		// m.addAttribute("title","Update Contact");
		Contact contact = this.contactRepository.findById(id).get();
		m.addAttribute("contact", contact);
		return "Normal/update_form";
	}

	// Save Update Contact Form
	@PostMapping("/update-contact-detail")
	public String updateHandler(@ModelAttribute("contact") Contact contact, Model model,
			@RequestParam("profileImage") MultipartFile multiFile, HttpSession session, Principal principal) {
		Contact oldContact = this.contactRepository.findById(contact.getcId()).get();

		try {

			// Image
			if (!multiFile.isEmpty()) {
				// File Work..
				// Rewrite
				// delete old photo
				File deleteFile = new ClassPathResource("static/Images").getFile();
				File file1 = new File(deleteFile, oldContact.getImageUrl());
				file1.delete();

				// update photo

				File newFile = new ClassPathResource("static/Images").getFile();
				Path path = Paths.get(newFile.getAbsolutePath() + File.separator + multiFile.getOriginalFilename());
				Files.copy(multiFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImageUrl(multiFile.getOriginalFilename());

			} else {
				contact.setImageUrl(oldContact.getImageUrl());

			}
			String userName = principal.getName();
			contact.setUser(this.userRepository.getUserByUserName(userName));
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Your Contact is update..", "success"));

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Your Contact is not update..", "danger"));

		}

		return "redirect:/user/" + contact.getcId() + "/contact";
	}

	@GetMapping("/profile")
	public String yourProfile(Model model) {

		return "Normal/profile";
	}

	@GetMapping("/test")
	public String yourNewProfile(Model model) {

		return "Normal/test";
	}

	// Open Setting Handler
	@GetMapping("/settings")
	public String openSetting() {
		return "Normal/settings";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			Principal principal, HttpSession session) {
		User user = this.userRepository.getUserByUserName(principal.getName());

		// if(this.bCryptPasswordEncoder.matches(oldPassword,user.getPasssword()))
		// change password
		// user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
		// this.userRepository.save(currentUser);
		//
		if (user.getPassword().equals(oldPassword)) {
			user.setPassword(newPassword);
			this.userRepository.save(user);
			session.setAttribute("message", new Message("Your Password Successfully Changed...", "success"));
		} else {
			session.setAttribute("message", new Message("Please Enter Right Password...", "danger"));
			return "redirect:/user/settings";
		}
		return "redirect:/user/index";
	}

	// Creating order for payment

	@PostMapping("/create_order")

	public String createORDER() {
		System.out.println("Run only code...");
		System.out.println("hrt eijr    ");
		return "";
	}

}
