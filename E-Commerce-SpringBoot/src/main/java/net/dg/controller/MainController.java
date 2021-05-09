package net.dg.controller;

import net.dg.model.Product;
import net.dg.model.User;
import net.dg.service.ProductService;
import net.dg.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
/*
    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) throws Exception {
        String category = request.getParameter("category");
        String sortBy = request.getParameter("sortBy");
        if (sortBy == null) {
            sortBy = "nameAsc";
        }
        List<Product> products;
        if (category == null || category.equals("all")) {
            products = productService.findAll();
            products = productService.sortProductsBy(products, sortBy);
            category = "all";
        } else {
            Long categoryId = Long.valueOf(category);
            products = productService.findAllByCategoryId(categoryId);
            products = productService.sortProductsBy(products, sortBy);
        }
        model.addAttribute("products", products);
        model.addAttribute("category", category);
        model.addAttribute("sortBy", sortBy);

        return "index";
    }
    */

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) throws Exception {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);


        return "index";
    }

}
