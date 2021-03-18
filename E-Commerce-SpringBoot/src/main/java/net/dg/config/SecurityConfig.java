package net.dg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import net.dg.service.CustomerService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Configuration
	@Order(1)
	public static class AdminConfig extends WebSecurityConfigurerAdapter {

		public AdminConfig() {
			super();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.requestMatcher(new AntPathRequestMatcher("/admin_login*"))
			.authorizeRequests().antMatchers(
					"/admin/**").hasRole("ADMIN")
			.and()
			.authorizeRequests().antMatchers(
					"/js/**",
					"/css/**",
					"/images/**",
					"/registration/**",
					"/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/admin_login")
			.loginProcessingUrl("/admin_login")
			.defaultSuccessUrl("/admin/products/show", true)
			.permitAll()
			.and()
			.logout()
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.deleteCookies("JSESSIONID")
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/admin_login?logout")
			.permitAll()
			.and()
			.exceptionHandling()
			.accessDeniedPage("/403");


		}


		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception
		{
			String password = passwordEncoder().encode("admin");
			auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser("admin@gmail.com").password(password).roles("ADMIN");
		}
	}

	@Configuration
	@Order(2)
	public static class UserConfig extends WebSecurityConfigurerAdapter {
		public UserConfig() {
			super();
		}

		@Autowired
		private CustomerService customerService;

		@Bean
		public DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
			auth.setUserDetailsService(customerService);
			auth.setPasswordEncoder(passwordEncoder());
			return auth;
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.requestMatcher(new AntPathRequestMatcher("/**"))
			.authorizeRequests().antMatchers(
					"/admin/**").hasRole("ADMIN")
			.and()
			.authorizeRequests().antMatchers(
					"/js/**",
					"/css/**",
					"/images/**",
					"/registration/**",
					"/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/products", true)
			.permitAll()
			.and()
			.logout()
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.deleteCookies("JSESSIONID")
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login?logout")
			.permitAll()
			.and()
			.exceptionHandling()
			.accessDeniedPage("/403");

		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception
		{

			auth.authenticationProvider(authenticationProvider());
		}
	}
}
