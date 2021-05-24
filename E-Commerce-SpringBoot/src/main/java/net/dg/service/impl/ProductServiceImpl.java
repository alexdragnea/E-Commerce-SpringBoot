package net.dg.service.impl;

import lombok.AllArgsConstructor;
import net.dg.exceptions.*;
import net.dg.entity.Product;
import net.dg.repository.ProductRepository;
import net.dg.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);

    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void deleteProductById(Long id) {
        this.productRepository.deleteById(id);

    }

    @Override
    public Page<Product> listAll(int pageNum) {
        int pageSize = 12;

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> findByKeyword(String keyword) throws ProductNotFoundException {

        if(productRepository.findByKeyword(keyword).isEmpty()){
            throw new ProductNotFoundException();
        }

        return productRepository.findByKeyword(keyword);
    }


}
