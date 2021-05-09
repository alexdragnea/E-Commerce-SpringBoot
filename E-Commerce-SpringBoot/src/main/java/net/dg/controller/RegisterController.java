package net.dg.controller;

import net.dg.model.User;
import net.dg.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/registration")
public class RegisterController {

	private UserService userService;


	public RegisterController(UserService userService) {
		super();
		this.userService = userService;
	}

	@ModelAttribute("user")
	public User customerRegistration() {
		return new User();
	}

	@GetMapping
	public String showRegistrationForm() {
		return "/login/registration";
	}

	@PostMapping
	public String registerCustomerAccount(@ModelAttribute("user") User user) {
		userService.saveNewUser(user);
		return "redirect:/registration?success";
	}
}
