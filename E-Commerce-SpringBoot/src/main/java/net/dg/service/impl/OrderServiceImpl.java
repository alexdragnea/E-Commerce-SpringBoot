package net.dg.service.impl;

import net.dg.model.Order;
import net.dg.model.OrderedProduct;
import net.dg.model.Product;
import net.dg.model.User;
import net.dg.repository.OrderRepository;
import net.dg.repository.OrderedProductRepository;
import net.dg.service.CartService;
import net.dg.service.OrderService;
import net.dg.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderedProductRepository orderedProductRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public OrderServiceImpl(OrderedProductRepository orderedProductRepository,
                            OrderRepository orderRepository,
                            CartService cartService, ProductService productService) {
        this.orderedProductRepository = orderedProductRepository;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    // TODO remake method
    @Override
    public void makeOrder(User user) {
        Map<Product, Integer> productsForOrder = cartService.getAllProductsInCart(user);

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
                System.out.println("eroare");

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

    @Override
    public Set<OrderedProduct> getAllOrderedProductsOfUser(User user) {
        Set<OrderedProduct> allOrderedProducts = new HashSet<>();

        Set<Order> allOrders = orderRepository.findAllByUser(user);
//        Set<Order> allOrders = user.getCart().getOrders();

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

    //TODO rewrite method
    @Override
    public void approveOrder(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow(Exception::new);

        Set<OrderedProduct> orderedProducts = order.getOrderedProducts();
        for (OrderedProduct x : orderedProducts) {
            if (x.getQuantity() > productService
                    .getProductById(x.getProductId())
                    .orElseThrow(Exception::new).getQuantity()) {
                System.out.println("EROARE");
            }
        }

        order.setApproved(true);
        orderRepository.save(order);

        for (OrderedProduct x : orderedProducts) {
            Product product = productService
                    .getProductById(x.getProductId())
                    .orElseThrow(Exception::new);
            product.setQuantity(product.getQuantity() - x.getQuantity());
            productService.saveProduct(product);
        }
    }


    @Override
    public void declineOrder(Long orderId) throws Exception {
        // Удалить ордер с данным айди, и заполнить корзину продуктами из ордера по айдишнику в ОрдередПродактс
        Order currentOrder = orderRepository.findOneById(orderId).orElseThrow(Exception::new);

        for (OrderedProduct x : currentOrder.getOrderedProducts()) {
            cartService.addProductToCart(currentOrder.getUser(), x.getProductId());
        }

        orderRepository.deleteById(orderId);
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        return orderRepository.findOneById(orderId).orElseThrow(Exception::new);
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
    public Set<OrderedProduct> findAllOrderedProductByOrderId(Long orderId) throws Exception {
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
