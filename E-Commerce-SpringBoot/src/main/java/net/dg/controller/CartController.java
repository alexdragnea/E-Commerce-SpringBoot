package net.dg.controller;

import lombok.AllArgsConstructor;
import net.dg.model.Product;
import net.dg.model.User;
import net.dg.service.CartService;
import net.dg.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@AllArgsConstructor
@Controller
public class CartController {

    final private OrderService orderService;
    final private CartService cartService;


    @GetMapping("/user/cart")
    public String cart(@AuthenticationPrincipal User user, Model model) {
        Map<Product, Integer> productsInCart = cartService.getAllProductsInCart(user);
        model.addAttribute("products", productsInCart);
        model.addAttribute("totalPrice", cartService.getTotal(productsInCart));
        model.addAttribute("approvedOrderedProducts", orderService.getAllApprovedOrderedProductsOfUser(user));
        model.addAttribute("notApprovedOrderedProducts", orderService.getAllNotApprovedOrderedProductsOfUser(user));
        return "/user/cart";
    }

    //TODO make validation of neededQuantity without error_page
    @PostMapping("/user/cart")
    public String updateNeededQuantity(@AuthenticationPrincipal User user,
                                       @RequestParam(value = "productId") Long productId,
                                       @RequestParam(value = "neededQuantity") Integer neededQuantity,
                                       Model model) throws Exception {
        try {
            cartService.updateNeededQuantity(user, productId, neededQuantity);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorString", e.getMessage());
            Map<Product, Integer> productsInCart = cartService.getAllProductsInCart(user);
            model.addAttribute("products", productsInCart);
            model.addAttribute("totalPrice", cartService.getTotal(productsInCart));
            model.addAttribute("approvedOrderedProducts", orderService.getAllApprovedOrderedProductsOfUser(user));
            model.addAttribute("notApprovedOrderedProducts", orderService.getAllNotApprovedOrderedProductsOfUser(user));
            return "/user/cart";
        }
        return "redirect:/user/cart";
    }

    @GetMapping("/user/cart/addproduct/{productId}")
    public String addProductToCart(@PathVariable("productId") Long productId,
                                   @AuthenticationPrincipal User user, Model model) throws Exception {

        cartService.addProductToCart(user, productId);
        return "redirect:/user/cart";
    }

    @GetMapping("/user/cart/removeproduct/{productId}")
    public String removeProductToCart(@PathVariable("productId") Long productId,
                                   @AuthenticationPrincipal User user) throws Exception {

        cartService.removeProductFromCart(user, productId);
        return "redirect:/user/cart";
    }

    //TODO think is StockIsNotEnough exception needed hear and rewrite
    @GetMapping("/user/cart/order")
    public String makeOrder(@AuthenticationPrincipal User user, Model model) {

        try {
            orderService.makeOrder(user);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorString", e.getMessage());
            Map<Product, Integer> productsInCart = cartService.getAllProductsInCart(user);
            model.addAttribute("products", productsInCart);
            model.addAttribute("totalPrice", cartService.getTotal(productsInCart));
            model.addAttribute("approvedOrderedProducts", orderService.getAllApprovedOrderedProductsOfUser(user));
            model.addAttribute("notApprovedOrderedProducts", orderService.getAllNotApprovedOrderedProductsOfUser(user));
            return "/user/cart";
        }

        return "redirect:/user/cart";
    }
}
