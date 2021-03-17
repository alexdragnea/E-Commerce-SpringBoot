package net.dg.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import net.dg.model.Customer;

public interface CustomerRepository extends 
		JpaRepository<Customer, Long>, CrudRepository<Customer, Long> {
	
//	List<Customer> findByKeyword(@Param("keyword") String keyword);
	
	Customer findByEmail(String email);
}
