package net.dg.controller;

import net.dg.model.Product;
import net.dg.service.ProductService;
import net.dg.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MainController {
    private final UserServiceImpl userServiceImpl;
    private final ProductService productService;

    @Autowired
    public MainController(UserServiceImpl userServiceImpl, ProductService productService) {
        this.userServiceImpl = userServiceImpl;
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) throws Exception {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("productList", productList);


        return "user/productlist";
    }

}
