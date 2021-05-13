package net.dg.controller;

import lombok.AllArgsConstructor;
import net.dg.exceptions.EmptyCartException;
import net.dg.exceptions.ProductAlreadyInCartException;
import net.dg.exceptions.StockIsNotEnoughException;
import net.dg.model.Product;
import net.dg.model.User;
import net.dg.service.CartService;
import net.dg.service.OrderService;
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

    private static final String PRODUCTS = "products";
    private static final String TOTAL_PRICE = "totalPrice";
    private static final String APPROVED_ORDERED_PRODUCTS = "approvedOrderedProducts";
    private static final String NOT_APPROVED_ORDERED_PRODUCTS = "notApprovedOrderedProducts";

    private static final String REDIRECT_USER_CART = "redirect:/user/cart";
    private static final String USER_CART = "/user/cart";

    private final OrderService orderService;
    private final CartService cartService;


    @GetMapping("/user/cart")
    public String cart(@AuthenticationPrincipal User user, Model model) {
        Map<Product, Integer> productsInCart = cartService.getAllProductsInCart(user);
        model.addAttribute(PRODUCTS, productsInCart);
        model.addAttribute(TOTAL_PRICE, cartService.getTotal(productsInCart));
        model.addAttribute(APPROVED_ORDERED_PRODUCTS, orderService.getAllApprovedOrderedProductsOfUser(user));
        model.addAttribute(NOT_APPROVED_ORDERED_PRODUCTS, orderService.getAllNotApprovedOrderedProductsOfUser(user));
        return USER_CART;
    }

    @PostMapping("/user/cart")
    public String updateNeededQuantity(@AuthenticationPrincipal User user,
                                       @RequestParam(value = "productId") Long productId,
                                       @RequestParam(value = "neededQuantity") Integer neededQuantity,
                                       Model model) throws Exception {

        try {
            cartService.updateNeededQuantity(user, productId, neededQuantity);
        } catch (StockIsNotEnoughException e) {
            e.printStackTrace();
            model.addAttribute("errorString", e.getMessage());
            Map<Product, Integer> productsInCart = cartService.getAllProductsInCart(user);
            model.addAttribute(PRODUCTS, productsInCart);
            model.addAttribute(TOTAL_PRICE, cartService.getTotal(productsInCart));
            model.addAttribute(APPROVED_ORDERED_PRODUCTS, orderService.getAllApprovedOrderedProductsOfUser(user));
            model.addAttribute(NOT_APPROVED_ORDERED_PRODUCTS, orderService.getAllNotApprovedOrderedProductsOfUser(user));
            return "error_page";
        }

        return REDIRECT_USER_CART;
    }

    @GetMapping("/user/cart/addproduct/{productId}")
    public String addProductToCart(@PathVariable("productId") Long productId,
                                   @AuthenticationPrincipal User user, Model model) throws Exception {

        try {
            cartService.addProductToCart(user, productId);
        } catch (ProductAlreadyInCartException e) {
            e.printStackTrace();
            model.addAttribute("errorString", e.getMessage());
            return "error_page";
        }
        return REDIRECT_USER_CART;
    }

    @GetMapping("/user/cart/removeproduct/{productId}")
    public String removeProductToCart(@PathVariable("productId") Long productId,
                                      @AuthenticationPrincipal User user) {

        cartService.removeProductFromCart(user, productId);
        return REDIRECT_USER_CART;
    }

    @GetMapping("/user/cart/order")
    public String makeOrder(@AuthenticationPrincipal User user, Model model) throws Exception {

        try {
            orderService.makeOrder(user);
        } catch (EmptyCartException | StockIsNotEnoughException e) {
            e.printStackTrace();
            model.addAttribute("errorString", e.getMessage());
            return "error_page";
        }

        return REDIRECT_USER_CART;
    }
}
