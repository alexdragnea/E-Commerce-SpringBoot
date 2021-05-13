package net.dg.repository;

import net.dg.model.Order;
import net.dg.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    Set<Order> findAllByUser(User user);
    Set<Order> findAllByUserAndIsApprovedIsTrue(User user);
    Set<Order> findAllByUserAndIsApprovedIsFalse(User user);

    Set<Order> findAllByIsApprovedIsFalse();
    Set<Order> findAllByIsApprovedIsTrue();

    Optional<Order> findOneById(Long orderId);

}
