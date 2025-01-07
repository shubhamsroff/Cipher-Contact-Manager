package com.shubh.controller;

import java.security.Principal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shubh.entities.Contact;
import com.shubh.entities.User;
import com.shubh.repository.ContactRepository;
import com.shubh.repository.UserRepository;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ContactRepository contactRepo;
		
//	Search Handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal){
		System.out.println(query);
		User user=this.userRepo.getUserByUserName(principal.getName());
		List<Contact> contacts=this.contactRepo.findByNameContainingAndUser(query, user);
	    System.out.println(contacts.get(0).getName());
		return ResponseEntity.ok(contacts);
	}
}
