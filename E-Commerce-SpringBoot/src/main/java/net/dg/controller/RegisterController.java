package net.dg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.dg.model.Customer;
import net.dg.service.CustomerService;

@Controller
@RequestMapping("/registration")
public class RegisterController {
	
	private CustomerService customerService;


	public RegisterController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}
	
	@ModelAttribute("user")
    public Customer customerRegistration() {
        return new Customer();
    }
	
	@GetMapping
	public String showRegistrationForm() {
		return "/login/registration";
	}
	
	@PostMapping
	public String registerCustomerAccount(@ModelAttribute("user") Customer customer) {
		customerService.saveCustomer(customer);
		return "redirect:/registration?success";
	}
}
