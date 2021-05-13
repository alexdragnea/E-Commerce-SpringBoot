package net.dg.controller;

import lombok.AllArgsConstructor;
import net.dg.model.Product;
import net.dg.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@AllArgsConstructor
@Controller
public class MainController {

    private final ProductService productService;


    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("productList", productList);


        return "user/productlist";
    }

}
