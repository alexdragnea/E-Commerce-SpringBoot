package net.dg.controller;

import lombok.AllArgsConstructor;
import net.dg.entity.Product;
import net.dg.exceptions.ProductNotFoundException;
import net.dg.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@AllArgsConstructor
@Controller
public class ViewController {

    private final ProductService productService;


    @GetMapping("/")
    public String home(Model model, String keyword) throws ProductNotFoundException {

        if (keyword != null) {
            try {
                model.addAttribute("products", productService.findByKeyword(keyword));
                return "user/productlist";
            } catch (ProductNotFoundException e) {
                e.printStackTrace();
                model.addAttribute("errorString", e.getMessage());
                return "error_page";
            }

        }

        return findPaginated(model, 1);
    }


    @RequestMapping("/page/{pageNum}")
    public String findPaginated(Model model,
                                @PathVariable(name = "pageNum") int pageNum) {

        Page<Product> page = productService.listAll(pageNum);

        List<Product> productList = page.getContent();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("products", productList);

        return "user/productlist";
    }

    @RequestMapping("/login")
    public String userLogin() {
        return "/login/login";
    }

    @GetMapping("/403")
    public String error403() {
        return "error403";
    }

}
