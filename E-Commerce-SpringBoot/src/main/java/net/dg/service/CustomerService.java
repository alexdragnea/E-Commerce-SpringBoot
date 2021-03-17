package net.dg.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import net.dg.model.Customer;

public interface CustomerService extends UserDetailsService {

	List<Customer> getAllCustomers();
	List<Customer> findByKeyboard(String keyboard);
	Customer saveCustomer(Customer customer);
	Customer getCustomerById(long id);
	void deleteCustomerById(long id);
	
}
