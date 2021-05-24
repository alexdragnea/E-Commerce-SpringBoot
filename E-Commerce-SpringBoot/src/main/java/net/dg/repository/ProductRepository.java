package net.dg.repository;

import net.dg.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends
        JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products p WHERE p.name like %:keyword% "
            + "or p.price like %:keyword% "
            + "or p.description like %:keyword% ",
            nativeQuery = true)
    List<Product> findByKeyword(@Param("keyword") String keyword);

}
