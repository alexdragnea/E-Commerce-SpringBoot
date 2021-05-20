package net.dg.controller;

import lombok.AllArgsConstructor;
import net.dg.model.ConfirmationToken;
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
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final static String FORGOT_PASSWORD = "login/forgotPassword";
    private final static String RESET_PASSWORD = "login/resetPassword";
    private final static String ERROR = "error";

    private final UserService userService;
    private final ProductService productService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    @GetMapping("/account")
    public String editOwnAccount(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "/user/user_account";
    }

    @GetMapping("/account/address")
    public String editOwnAddress(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user.getAddress());
        return "/user/user_address";
    }

    @PostMapping("/account/{userId}")
    public String updateUserInfo(@AuthenticationPrincipal User user,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String password) {

        userService.updateUser(user, firstName, lastName, password);
        return "redirect:/user/cart";
    }

    @PostMapping("/account/address/{addressId}")
    public String updateUserAddress(@AuthenticationPrincipal User user,
                                    @RequestParam String streetName,
                                    @RequestParam String streetNumber,
                                    @RequestParam String city,
                                    @RequestParam String contact,
                                    @RequestParam String zipCode) {

        userService.updateAddress(user, streetName, streetNumber, city, contact, zipCode);
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

    @GetMapping("/forgot-password")
    public ModelAndView displayResetPassword(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName(FORGOT_PASSWORD);
        return modelAndView;
    }

    @PostMapping("/forgot-password")
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
            modelAndView.addObject(ERROR, "This email does not exist!");
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
            modelAndView.addObject(ERROR, "The link is invalid or broken!");
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
            modelAndView.addObject(ERROR, "The link is invalid or broken!");
            modelAndView.setViewName(RESET_PASSWORD);
        }
        return modelAndView;
    }

}
