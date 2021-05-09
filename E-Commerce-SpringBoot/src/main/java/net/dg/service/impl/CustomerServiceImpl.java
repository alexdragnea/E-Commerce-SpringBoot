package net.dg.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.dg.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import net.dg.model.Customer;
import net.dg.model.Role;
import net.dg.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public CustomerServiceImpl(CustomerRepository customerRepository) {
		super();
		this.customerRepository = customerRepository;
	}

	@Override
	public List<Customer> getAllCustomers() {
		// TODO Auto-generated method stub
		return customerRepository.findAll();
	}

	@Override
	public List<Customer> findByKeyboard(String keyboard) {

		return null;
	}

	@Override
	public Customer getCustomerById(long id) {
		Optional<Customer> optional = customerRepository.findById(id);
		Customer customer = null;
		if(optional.isPresent()) {
			customer = optional.get();
		}
		else {
			throw new RuntimeException(" Customer with id: " + id + " not found");
		}
		return customer;
	}

	@Override
	public void deleteCustomerById(long id) {
		this.customerRepository.deleteById(id);

	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer customer = customerRepository.findByEmail(username);

		if(customer == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(customer.getEmail(), customer.getPassword(), mapRolesToAuthorities(customer.getRoles()));	
	}

	public Customer saveCustomer(Customer customerDTO) {
		Customer customer = new Customer(customerDTO.getFullName(), customerDTO.getEmail(), 
				passwordEncoder.encode(customerDTO.getPassword()),
				customerDTO.getContact(), Arrays.asList(new Role("USER")));
		return customerRepository.save(customer);
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

}
