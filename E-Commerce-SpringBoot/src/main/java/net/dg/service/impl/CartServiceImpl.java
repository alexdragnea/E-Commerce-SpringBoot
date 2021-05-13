package net.dg.service.impl;

import lombok.AllArgsConstructor;
import net.dg.exceptions.ProductAlreadyInCartException;
import net.dg.model.Cart;
import net.dg.model.InCartProduct;
import net.dg.model.Product;
import net.dg.model.User;
import net.dg.repository.CartRepository;
import net.dg.repository.InCartProductRepository;
import net.dg.repository.ProductRepository;
import net.dg.service.CartService;
import net.dg.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final CartRepository cartRepository;
    private final InCartProductRepository inCartProductRepository;


    @Transactional
    @Override
    public void removeProductFromCart(User user, Long productId) {
        inCartProductRepository.deleteByCartAndProductId(user.getCart(), productId);
    }

    @Transactional
    @Override
    public void addProductToCart(User user, Long productId) throws Exception {
        Product product = productService.getProductById(productId).orElseThrow(Exception::new);

        Cart cart = cartRepository.findById(user.getCart().getId()).orElseThrow(Exception::new);

        Optional<InCartProduct> optionalInCartProduct = inCartProductRepository.findByCartAndProductId(user.getCart(), productId);
        if (optionalInCartProduct.isPresent()) {
            throw new ProductAlreadyInCartException();
        } else {
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


    @Override
    public void updateNeededQuantity(User user, Long productId, Integer neededQuantity) {
        InCartProduct inCartProduct = inCartProductRepository
                .findByCartAndProductId(user.getCart(), productId).get();
        Product product = productRepository.getOne(productId);
        inCartProduct.setNeededQuantity(neededQuantity);
        inCartProductRepository.save(inCartProduct);
    }
}
