package net.dg.service.impl;

import lombok.AllArgsConstructor;
import net.dg.exceptions.ProductAlreadyInCartException;
import net.dg.exceptions.StockIsNotEnoughException;
import net.dg.entity.Cart;
import net.dg.entity.InCartProduct;
import net.dg.entity.Product;
import net.dg.entity.User;
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
    public void addProductToCart(User user, Long productId) throws ProductAlreadyInCartException {
        Product product = productService.getProductById(productId).orElseThrow(ProductAlreadyInCartException::new);

        Cart cart = cartRepository.findById(user.getCart().getId()).orElseThrow(ProductAlreadyInCartException::new);

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
                        .toMap(InCartProduct::getProduct,
                                InCartProduct::getNeededQuantity));
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
    public void updateNeededQuantity(User user, Long productId, Integer neededQuantity) throws StockIsNotEnoughException{
        InCartProduct inCartProduct = inCartProductRepository
                .findByCartAndProductId(user.getCart(), productId).orElseThrow(StockIsNotEnoughException::new);
        Product product = productRepository.getOne(productId);

        if(neededQuantity <= product.getQuantity()) {
            inCartProduct.setNeededQuantity(neededQuantity);
            inCartProductRepository.save(inCartProduct);
        } else {
                throw new StockIsNotEnoughException();
        }
    }
}
