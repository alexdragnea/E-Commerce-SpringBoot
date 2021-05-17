package net.dg.controller;

import lombok.AllArgsConstructor;
import net.dg.model.ConfirmationToken;
import net.dg.model.User;
import net.dg.repository.ConfirmationTokenRepository;
import net.dg.repository.UserRepository;
import net.dg.service.EmailService;
import net.dg.service.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;


@AllArgsConstructor
@Controller
@RequestMapping("/registration")
public class RegisterController {

    private final UserService userService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @ModelAttribute("user")
    public User customerRegistration() {
        return new User();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "/register/registration";
    }

    @PostMapping
    public ModelAndView registerCustomerAccount(@ModelAttribute("user") @Valid User user,
                                                BindingResult bindingResult, ModelAndView modelAndView) {


        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register/registration");
        } else {

            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                modelAndView.addObject("message", "This email already exists!");
                modelAndView.setViewName("register/registration");
            } else {

                userService.saveNewUser(user);
                ConfirmationToken confirmationToken = new ConfirmationToken(user);
                confirmationTokenRepository.save(confirmationToken);
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Complete Registration");
                mailMessage.setFrom("javaprojects1999@gmail.com");
                mailMessage.setText("To confirm your account, please click here: "
                        + "http://localhost:8080/user/confirm-account?token="
                        + confirmationToken.getConfirmationToken());

                emailService.sendEmail(mailMessage);
                modelAndView.addObject("email", user.getEmail());
                modelAndView.setViewName("register/succesfulRegistration");

            }
        }

        return modelAndView;
    }
}