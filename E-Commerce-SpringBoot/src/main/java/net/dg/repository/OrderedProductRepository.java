package net.dg.repository;


import net.dg.entity.Order;
import net.dg.entity.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {

    Set<OrderedProduct> findAllByOrder(Order order);
}
