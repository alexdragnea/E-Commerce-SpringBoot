package net.dg.repository;


import net.dg.model.Cart;
import net.dg.model.InCartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface InCartProductRepository extends JpaRepository<InCartProduct, Long> {
    Set<InCartProduct> findAllByCart(Cart cart);
    void deleteAllByCart(Cart cart);
    void deleteByCartAndProductId(Cart cart, Long productId);
    Optional<InCartProduct> findByCartAndProductId(Cart cart, Long productId);
}
