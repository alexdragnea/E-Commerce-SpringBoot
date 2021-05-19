package net.dg.controller;

import lombok.AllArgsConstructor;
import net.dg.model.Product;
import net.dg.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@AllArgsConstructor
@Controller
public class MainController {

    private final ProductService productService;


    @GetMapping("/")
    public String home(Model model, String keyword) {

        if (keyword != null) {
            model.addAttribute("productList", productService.findByKeyword(keyword));
            return "user/productlist";
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
        model.addAttribute("productList", productList);

        return "user/productlist";
    }

}
