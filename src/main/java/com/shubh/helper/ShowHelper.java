package com.shubh.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.http.HttpSession;

@Component
public class ShowHelper {
	public void removeMessageFromSession() {
		try {
			System.out.println("remove ");
			HttpSession session = (HttpSession) RequestContextHolder.getRequestAttributes().getSessionMutex();
			session.removeAttribute("message");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
