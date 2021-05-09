package net.dg.controller;

import net.dg.model.OrderedProduct;
import net.dg.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/orders")
    public String allOrders(Model model) {
        model.addAttribute("notApprovedOrders", orderService.findAllByApprovedFalse());
        model.addAttribute("approvedOrders", orderService.findAllByApprovedTrue());
        return "/admin/orders";
    }

    //TODO think about StockIsNotEnoughException
    @GetMapping("/admin/orders/approve")
    public String approveOrder(@RequestParam("orderId") Long orderId, Model model) throws Exception{

        try {
            orderService.approveOrder(orderId);
        } catch (Exception e) {
            model.addAttribute("errorString", e.getMessage());
            return "error_page";
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
    public String declineOrder(@RequestParam("orderId") Long orderId) throws Exception{
        orderService.declineOrder(orderId);
        return "redirect:/admin/orders";
    }
}
