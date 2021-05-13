package net.dg.controller;

import lombok.AllArgsConstructor;
import net.dg.model.ConfirmationToken;
import net.dg.model.Product;
import net.dg.model.User;
import net.dg.repository.ConfirmationTokenRepository;
import net.dg.repository.UserRepository;
import net.dg.service.EmailService;
import net.dg.service.ProductService;
import net.dg.service.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final static String FORGOT_PASSWORD = "login/forgotPassword";
    private final static String RESET_PASSWORD = "login/resetPassword";
    private final static String ERROR = "error";

    private UserService userService;
    private ProductService productService;
    private ConfirmationTokenRepository confirmationTokenRepository;
    private UserRepository userRepository;
    private EmailService emailService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping("/")
    public String userHome(@AuthenticationPrincipal User user, HttpServletRequest request, Model model) {
        List<Product> products = productService.getAllProducts();

        model.addAttribute("products", products);

        return "/user/user_home";
    }

    @GetMapping("/account")
    public String editOwnAccount(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "/user/user_account";
    }

    @PostMapping("/account/{userId}")
    public String updateUserInfo(@AuthenticationPrincipal User user,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String password,
                                 @RequestParam String city,
                                 @RequestParam String street,
                                 @RequestParam String streetNumber,
                                 @RequestParam String phoneNumber) {

        userService.updateUser(user, firstName, lastName, password, city, street, streetNumber, phoneNumber);
        return "redirect:/user/cart";
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken) {

        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            Optional<User> optional = userRepository.findByEmail(token.getUser().getEmail());
            if (optional.isPresent()) {

                User user = optional.get();
                user.setAccountUnLocked();
                userRepository.save(user);
                modelAndView.setViewName("register/accountVerified");
            }
        } else {
            modelAndView.addObject("message", "true");
            modelAndView.setViewName("register/accountVerified");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
    public ModelAndView displayResetPassword(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName(FORGOT_PASSWORD);
        return modelAndView;
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public ModelAndView forgotUserPassword(ModelAndView modelAndView, User user) {
        Optional<User> optional = userRepository.findByEmail(user.getEmail());

        if (optional.isPresent()) {

            User existingUser = optional.get();
            ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);
            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(existingUser.getEmail());
            mailMessage.setSubject("Complete Password Reset");
            mailMessage.setFrom("javaprojects1999@gmail.com");
            mailMessage.setText("Tom complete the password reset, please click here: "
                    + "http://localhost:8080/user/confirm-reset?token=" + confirmationToken.getConfirmationToken());

            emailService.sendEmail(mailMessage);

            modelAndView.addObject("succes", "Request to reset password received" +
                    ", check your inbox for the reset link.");
            modelAndView.setViewName(FORGOT_PASSWORD);
        } else {
            modelAndView.addObject("ERROR", "This email does not exist!");
            modelAndView.setViewName(FORGOT_PASSWORD);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/confirm-reset", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView validateResetToken(ModelAndView modelAndView,
                                           @RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            Optional<User> optional = userRepository.findByEmail(token.getUser().getEmail());
            User user = optional.get();

            userRepository.save(user);
            modelAndView.addObject("user", user);
            modelAndView.addObject("email", user.getEmail());
            modelAndView.setViewName(RESET_PASSWORD);
        } else {
            modelAndView.addObject("ERROR", "The link is invalid or broken!");
            modelAndView.setViewName(RESET_PASSWORD);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ModelAndView resetUserPassword(ModelAndView modelAndView, User user) {

        if (user.getEmail() != null) {
            Optional<User> optional = userRepository.findByEmail(user.getEmail());
            User tokenUser = optional.get();
            tokenUser.setPassword(encoder.encode(user.getPassword()));

            userRepository.save(tokenUser);
            modelAndView.addObject("succes", "Password succesfully reseted." +
                    "You can now log in with the new credentials.");
            modelAndView.setViewName(RESET_PASSWORD);
        } else {
            modelAndView.addObject("ERROR", "The link is invalid or broken!");
            modelAndView.setViewName(RESET_PASSWORD);
        }
        return modelAndView;
    }

}
