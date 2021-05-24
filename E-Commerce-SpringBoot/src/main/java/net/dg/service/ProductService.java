package net.dg.service;

import net.dg.exceptions.ProductNotFoundException;
import net.dg.entity.*;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    void saveProduct(Product product);
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    List<Product> findByKeyword(String keyword) throws ProductNotFoundException;
    void deleteProductById(Long id);
    Page<Product> listAll(int pageNum);

}
