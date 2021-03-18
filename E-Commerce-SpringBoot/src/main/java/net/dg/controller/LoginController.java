package net.dg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class LoginController {

	@RequestMapping("/admin_login")
	public String AdminLogin() {
		return "/login/adminlogin";
	}
	
	@RequestMapping("/login")
	public String UserLogin() {
		return "/login/customerlogin";
	}
	
	@GetMapping("/403")
	public String Error403() {
		return "error403";
	}
}
