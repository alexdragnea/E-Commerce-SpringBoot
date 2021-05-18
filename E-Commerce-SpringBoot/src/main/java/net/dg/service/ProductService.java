package net.dg.service;

import net.dg.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    void saveProduct(Product product);
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    List<Product> findByKeyword(String keyword);
    void deleteProductById(Long id);

}
