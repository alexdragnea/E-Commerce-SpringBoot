package net.dg.service;


import net.dg.model.Order;
import net.dg.model.OrderedProduct;
import net.dg.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


public interface OrderService {

    void makeOrder(User user) throws Exception;
    Set<OrderedProduct> getAllOrderedProductsOfUser(User user);
    List<OrderedProduct> getAllApprovedOrderedProductsOfUser(User user);
    List<OrderedProduct> getAllNotApprovedOrderedProductsOfUser(User user);

    Set<Order> findAll();
    Set<Order> findAllByApprovedFalse();
    Set<Order> findAllByApprovedTrue();
    void approveOrder(Long orderId) throws Exception;
    Order findOrderById(Long orderId) throws Exception;

    void saveProduct(OrderedProduct orderedProduct);
    Set<OrderedProduct> findAllOrderedProductByOrder(Order order);
    Set<OrderedProduct> findAllOrderedProductByOrderId(Long orderId) throws Exception;

    BigDecimal getTotal(Set<OrderedProduct> products);

    void declineOrder(Long orderId) throws Exception;

}
