package net.dg.controller;

import lombok.AllArgsConstructor;
import net.dg.model.Order;
import net.dg.model.OrderedProduct;
import net.dg.model.User;
import net.dg.service.EmailService;
import net.dg.service.OrderService;
import net.dg.service.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/admin/orders")
    public String allOrders(Model model) {
        model.addAttribute("notApprovedOrders", orderService.findAllByApprovedFalse());
        model.addAttribute("approvedOrders", orderService.findAllByApprovedTrue());
        return "/admin/orders";
    }

    @GetMapping("/admin/orders/approve")
    public String approveOrder(@RequestParam("orderId") Long orderId, Model model) throws Exception {

            Order order = orderService.findOrderById(orderId);
            Optional<User> optional = userService.findById(order.getUser().getId());

            if(optional.isPresent()) {
                User user = optional.get();
                orderService.approveOrder(orderId);

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("E-Commerce order " + order.getId());
                mailMessage.setFrom("javaprojects1999@gmail.com");
                mailMessage.setText("Your order has been approved and will be delivered shortly." +
                        "For viewing your order please go to http://localhost:8080/user/cart");
                emailService.sendEmail(mailMessage);
            }


        return "redirect:/admin/orders";
    }

    @GetMapping("/admin/orders/{orderId}")
    public String orderInfo(@PathVariable("orderId") Long orderId, Model model) throws Exception {
        Set<OrderedProduct> orderedProducts = orderService.findAllOrderedProductByOrderId(orderId);
        model.addAttribute("orderedProducts", orderedProducts);
        model.addAttribute("totalPrice", orderService.getTotal(orderedProducts));
        return "/admin/order_info";
    }

    @GetMapping("/admin/orders/decline")
    public String declineOrder(@RequestParam("orderId") Long orderId) throws Exception {
        orderService.declineOrder(orderId);
        return "redirect:/admin/orders";
    }
}
