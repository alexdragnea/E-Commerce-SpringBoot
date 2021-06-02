package net.dg.service;


import net.dg.exceptions.AddressNotFoundException;
import net.dg.exceptions.EmptyCartException;
import net.dg.exceptions.StockIsNotEnoughException;
import net.dg.entity.Order;
import net.dg.entity.OrderedProduct;
import net.dg.entity.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


public interface OrderService {

    void makeOrder(User user) throws EmptyCartException, StockIsNotEnoughException, AddressNotFoundException;
    Set<OrderedProduct> getAllOrderedProductsOfUser(User user);
    List<OrderedProduct> getAllApprovedOrderedProductsOfUser(User user);
    List<OrderedProduct> getAllNotApprovedOrderedProductsOfUser(User user);

    Set<Order> findAll();
    Set<Order> findAllByApprovedFalse();
    Set<Order> findAllByApprovedTrue();
    void approveOrder(Long orderId) throws IllegalArgumentException;
    Order findOrderById(Long orderId) throws IllegalArgumentException;

    void saveProduct(OrderedProduct orderedProduct);
    Set<OrderedProduct> findAllOrderedProductByOrder(Order order);
    Set<OrderedProduct> findAllOrderedProductByOrderId(Long orderId);

    BigDecimal getTotal(Set<OrderedProduct> products);

    void declineOrder(Long orderId) throws IllegalArgumentException;

}
