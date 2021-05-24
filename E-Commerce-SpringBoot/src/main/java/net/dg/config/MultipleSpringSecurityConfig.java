package net.dg.config;

import net.dg.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class MultipleSpringSecurityConfig {

    private static final String ADMIN = "ADMIN";

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @Order(1)
    public class AdminConfig extends WebSecurityConfigurerAdapter {


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests().antMatchers(
                    "/admin/**", "/admin*").hasRole(ADMIN)
                    .and()
                    .authorizeRequests().antMatchers(
                    "/", "/registration/**",
                    "/login/**",
                    "/js/**",
                    "/css/**",
                    "/images/**",
                    "/api/**",
                    "/product/**",
                    "/page/**",
                    "/user/confirm-account/**",
                    "/user/confirm-reset/**",
                    "/user/forgot-password/**",
                    "/user/reset-password/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/", true)
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
                    .csrf().disable();


        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

            auth.inMemoryAuthentication().withUser("admin@admin.com").
                    password(bCryptPasswordEncoder().encode("admin")).roles(ADMIN);

        }

    }

    @Configuration
    @Order(2)
    public class UserConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private UserServiceImpl userServiceImpl;


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests().antMatchers(
                    "/user/**", "/user**").hasRole("USER")
                    .antMatchers("/admin/**", "/admin*").hasRole(ADMIN)
                    .and()
                    .authorizeRequests().antMatchers(
                    "/", "/registration/**",
                    "/login/**",
                    "/js/**",
                    "/css/**",
                    "/images/**",
                    "/api/**",
                    "/product/**",
                    "/page/**",
                    "/user/confirm-account/**",
                    "/user/confirm-reset/**",
                    "/user/forgot-password/**",
                    "/user/reset-password/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/", true)
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
                    .csrf().disable();


        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

            auth
                    .userDetailsService(userServiceImpl).passwordEncoder(bCryptPasswordEncoder());

        }
    }

}
