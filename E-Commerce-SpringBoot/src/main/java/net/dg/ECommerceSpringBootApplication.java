package net.dg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@SpringBootApplication
public class ECommerceSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceSpringBootApplication.class, args);
	}

}
