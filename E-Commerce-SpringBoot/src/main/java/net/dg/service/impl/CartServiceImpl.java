package net.dg.service.impl;

import net.dg.model.Cart;
import net.dg.model.InCartProduct;
import net.dg.model.Product;
import net.dg.model.User;
import net.dg.repository.CartRepository;
import net.dg.repository.InCartProductRepository;
import net.dg.repository.ProductRepository;
import net.dg.service.CartService;
import net.dg.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    final private ProductRepository productRepository;
    final private UserServiceImpl userServiceImpl;
    final private ProductService productService;
    final private CartRepository cartRepository;
    final private InCartProductRepository inCartProductRepository;

    @Autowired
    public CartServiceImpl(ProductRepository productRepository, UserServiceImpl userServiceImpl,
                           ProductService productService,
                           CartRepository cartRepository, InCartProductRepository inCartProductRepository) {
        this.productRepository = productRepository;
        this.userServiceImpl = userServiceImpl;
        this.productService = productService;
        this.cartRepository = cartRepository;
        this.inCartProductRepository = inCartProductRepository;
    }

    @Transactional
    @Override
    public void removeProductFromCart(User user, Long productId) {
        inCartProductRepository.deleteByCartAndProductId(user.getCart(), productId);
    }

    // TODO make new Exception for this method and adding quantity
    @Transactional
    @Override
    public void addProductToCart(User user, Long productId) throws Exception {
        Product product = productService.getProductById(productId).orElseThrow(Exception::new);

        Cart cart = cartRepository.findById(user.getCart().getId()).orElseThrow(Exception::new);

        Optional<InCartProduct> optionalInCartProduct = inCartProductRepository.findByCartAndProductId(user.getCart(), productId);
        if (optionalInCartProduct.isPresent())
            System.out.println("product already in cart");
        else {
            InCartProduct inCartProduct = new InCartProduct();
            inCartProduct.setProduct(product);
            inCartProduct.setCart(cart);
            inCartProductRepository.save(inCartProduct);
        }
    }

    @Override
    public Map<Product, Integer> getAllProductsInCart(User user) {
        return inCartProductRepository.findAllByCart(user.getCart()).stream()
                .collect(Collectors
                        .toMap(inCartProduct -> inCartProduct.getProduct(),
                                productQuantity -> productQuantity.getNeededQuantity()));
    }

    @Override
    public BigDecimal getTotal(Map<Product, Integer> productsWithNeededQuantity) {
        return productsWithNeededQuantity.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Transactional
    @Override
    public void clearProductsFromCart(User user) {
        inCartProductRepository.deleteAllByCart(user.getCart());
    }

    //TODO make new Exception (Product has not been founded in cart)
    @Override
    public void updateNeededQuantity(User user, Long productId, Integer neededQuantity) throws Exception {
        InCartProduct inCartProduct = inCartProductRepository
                .findByCartAndProductId(user.getCart(), productId).orElseThrow(Exception::new);
        Product product = productRepository.getOne(productId);
        if (neededQuantity <= product.getQuantity()) {
            inCartProduct.setNeededQuantity(neededQuantity);
            inCartProductRepository.save(inCartProduct);
        } else {
            System.out.println("EROARE");;
        }
    }
}
