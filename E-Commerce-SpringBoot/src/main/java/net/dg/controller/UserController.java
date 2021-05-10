package net.dg.controller;

import net.dg.model.Product;
import net.dg.model.ShippingAddress;
import net.dg.model.User;
import net.dg.service.ProductService;
import net.dg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PreUpdate;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public UserController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

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

//    @GetMapping("/account/shippingaddress")
//    public String editOwnAccount(@AuthenticationPrincipal ShippingAddress shippingAddress, Model model) {
//        model.addAttribute("shippingAddress", shippingAddress);
//        return "/user/user_shippingaddress";
//    }

    // TODO: 1. Make validation of fields.
    @PostMapping("/account/{userId}")
    public String updateUserInfo(@AuthenticationPrincipal User user,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String password) {

        userService.updateUser(user, firstName, lastName, password);
        return "redirect:/user/cart";
    }

//    // TODO: 1. Make validation of fields.
//    @PostMapping("/account/shippingaddress/{shippingId}")
//    public String updateUserInfo(@AuthenticationPrincipal ShippingAddress shippingAddress,
//                                 @RequestParam String streetName,
//                                 @RequestParam String apartmentNumber,
//                                 @RequestParam String city,
//                                 @RequestParam String state,
//                                 @RequestParam String country,
//                                 @RequestParam String zipCode,
//                                 @RequestParam String contact) {
//
//        userService.updateUser(streetName, apartmentNumber, apartmentNumber, city, state
//        , country, zipCode, contact);
//        return "redirect:/user/cart";
//    }

}
