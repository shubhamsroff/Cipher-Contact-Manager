//package com.shubh.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import com.shubh.repository.UserRepository;
//import com.shubh.entities.User;
//public class UserDetailServiceImple implements UserDetailsService{
//
//	@Autowired
//	private UserRepository userRepository;
//	public UserDetailServiceImple(UserRepository userRepository) {
//		super();
//		this.userRepository = userRepository;
//	}
//	public UserDetailServiceImple() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//	
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//		// fetching user from database		
//		User user=userRepository.getUserByUserName(username);
//		if(user==null)
//		{
//			throw new UsernameNotFoundException("Could Not Find User");
//		}
//		CustomUserDetails customUserDetails=new CustomUserDetails(user);
//		
//		return customUserDetails;
//	}
//
//}
