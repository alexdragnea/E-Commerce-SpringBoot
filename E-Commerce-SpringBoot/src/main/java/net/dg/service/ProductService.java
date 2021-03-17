package net.dg.service;

import java.util.List;
import java.util.Optional;

import net.dg.model.Product;

public interface ProductService {

	void saveProduct(Product product);
	List<Product> getAllProducts();
	Optional<Product> getProductById(Long id);
	void deleteProductById(Long id);
	
}
