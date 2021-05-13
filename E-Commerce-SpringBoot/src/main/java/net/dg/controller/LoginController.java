package net.dg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {


    @RequestMapping("/login")
    public String userLogin() {
        return "/login/login";
    }

    @GetMapping("/403")
    public String error403() {
        return "error403";
    }
}
