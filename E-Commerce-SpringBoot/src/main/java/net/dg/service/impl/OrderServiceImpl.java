package net.dg.service.impl;

import lombok.AllArgsConstructor;
import net.dg.exceptions.AddressNotFoundException;
import net.dg.exceptions.EmptyCartException;
import net.dg.exceptions.StockIsNotEnoughException;
import net.dg.entity.*;
import net.dg.repository.OrderRepository;
import net.dg.repository.OrderedProductRepository;
import net.dg.service.CartService;
import net.dg.service.OrderService;
import net.dg.service.ProductService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderedProductRepository orderedProductRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;

    private static final String ORDER_NOT_FOUND = "Order cannot be found.";


    @Override
    public void makeOrder(User user) throws EmptyCartException, StockIsNotEnoughException, AddressNotFoundException {
        Map<Product, Integer> productsForOrder = cartService.getAllProductsInCart(user);
        ShippingAddress existingAddress = user.getAddress();

        if (productsForOrder.isEmpty()) {
            throw new EmptyCartException();
        } else if (existingAddress.isEmpty()) {
            throw new AddressNotFoundException();
        } else {

            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(new Date());
            orderRepository.save(order);

            Set<OrderedProduct> orderedProducts = new HashSet<>();
            for (Map.Entry<Product, Integer> entry : productsForOrder.entrySet()) {
                OrderedProduct orderedProduct = new OrderedProduct();
                orderedProduct.setProductId(entry.getKey().getId());
                orderedProduct.setName(entry.getKey().getName());
                if (entry.getValue() <= entry.getKey().getQuantity())
                    orderedProduct.setQuantity(entry.getValue());
                else
                    throw new StockIsNotEnoughException();

                orderedProduct.setPrice(entry.getKey().getPrice());
                orderedProduct.setDescription(entry.getKey().getDescription());
                orderedProduct.setOrder(order);
                orderedProducts.add(orderedProduct);
                saveProduct(orderedProduct);
            }

            order.setOrderedProducts(orderedProducts);
            order.setCart(user.getCart());
            orderRepository.save(order);

            cartService.clearProductsFromCart(user);
        }

    }

    @Override
    public Set<OrderedProduct> getAllOrderedProductsOfUser(User user) {
        Set<OrderedProduct> allOrderedProducts = new HashSet<>();

        Set<Order> allOrders = orderRepository.findAllByUser(user);

        for (Order x : allOrders) {
            allOrderedProducts.addAll(x.getOrderedProducts());
        }

        return allOrderedProducts;
    }

    @Override
    public List<OrderedProduct> getAllApprovedOrderedProductsOfUser(User user) {
        List<OrderedProduct> allApprovedOrderedProducts = new ArrayList<>();
        Set<Order> allApprovedOrdersOfUser = orderRepository.findAllByUserAndIsApprovedIsTrue(user);

        for (Order x : allApprovedOrdersOfUser) {
            allApprovedOrderedProducts.addAll(x.getOrderedProducts());
        }
        allApprovedOrderedProducts.sort(Comparator.comparing(o -> o.getOrder().getId()));
        return allApprovedOrderedProducts;
    }

    @Override
    public List<OrderedProduct> getAllNotApprovedOrderedProductsOfUser(User user) {
        List<OrderedProduct> allNotApprovedOrderedProducts = new ArrayList<>();
        Set<Order> allNotApprovedOrdersOfUser = orderRepository.findAllByUserAndIsApprovedIsFalse(user);

        for (Order x : allNotApprovedOrdersOfUser) {
            allNotApprovedOrderedProducts.addAll(x.getOrderedProducts());
        }
        allNotApprovedOrderedProducts.sort(Comparator.comparing(o -> o.getOrder().getId()));
        return allNotApprovedOrderedProducts;
    }

    @Override
    public Set<Order> findAll() {
        return new HashSet<>(orderRepository.findAll());
    }

    @Override
    public Set<Order> findAllByApprovedFalse() {
        return orderRepository.findAllByIsApprovedIsFalse();
    }

    @Override
    public Set<Order> findAllByApprovedTrue() {
        return orderRepository.findAllByIsApprovedIsTrue();
    }

    @Override
    public void approveOrder(Long orderId) throws IllegalArgumentException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        Set<OrderedProduct> orderedProducts = order.getOrderedProducts();
        for (OrderedProduct x : orderedProducts) {
            if (x.getQuantity() > productService
                    .getProductById(x.getProductId())
                    .orElseThrow(StockIsNotEnoughException::new).getQuantity()) {
                throw new StockIsNotEnoughException();
            }
        }

        order.setApproved(true);
        orderRepository.save(order);

        for (OrderedProduct x : orderedProducts) {
            Product product = productService
                    .getProductById(x.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product cannot be found."));
            product.setQuantity(product.getQuantity() - x.getQuantity());
            productService.saveProduct(product);
        }
    }


    @Override
    public void declineOrder(Long orderId) throws IllegalArgumentException {
        Order currentOrder = orderRepository.findOneById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        for (OrderedProduct x : currentOrder.getOrderedProducts()) {
            cartService.addProductToCart(currentOrder.getUser(), x.getProductId());
        }

        orderRepository.deleteById(orderId);
    }

    @Override
    public Order findOrderById(Long orderId) throws IllegalArgumentException {
        return orderRepository.findOneById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));
    }


    @Override
    public void saveProduct(OrderedProduct orderedProduct) {
        orderedProductRepository.save(orderedProduct);
    }

    @Override
    public Set<OrderedProduct> findAllOrderedProductByOrder(Order order) {
        return orderedProductRepository.findAllByOrder(order);
    }

    @Override
    public Set<OrderedProduct> findAllOrderedProductByOrderId(Long orderId) {
        return findAllOrderedProductByOrder(findOrderById(orderId));
    }

    @Override
    public BigDecimal getTotal(Set<OrderedProduct> products) {
        return products.stream()
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}

