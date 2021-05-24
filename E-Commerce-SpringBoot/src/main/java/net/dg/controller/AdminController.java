package net.dg.controller;

import lombok.AllArgsConstructor;
import net.dg.entity.User;
import net.dg.service.ProductService;
import net.dg.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String REDIRECT_ADMIN_USER = "redirect:/admin/users";

    private final UserService userService;
    private final ProductService productService;

    @GetMapping("")
    public String adminHome() {
        return "/admin/admin_home";
    }

    @GetMapping("/users")
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/admin/users";
    }

    @GetMapping("/users/delete")
    public String removeUser(@RequestParam("userId") Long userId) throws Exception {
        Optional<User> userFromDB = userService.findById(userId);
        User user = userFromDB.orElseThrow(Exception::new);
        userService.delete(user);
        return REDIRECT_ADMIN_USER;
    }

    @GetMapping("/users/block")
    public String blockUser(@RequestParam("userId") Long userId) {
        userService.blockUser(userId);
        return REDIRECT_ADMIN_USER;
    }

    @GetMapping("/users/unblock")
    public String unBlockUser(@RequestParam("userId") Long userId) {
        userService.unblockUser(userId);
        return REDIRECT_ADMIN_USER;
    }


}
