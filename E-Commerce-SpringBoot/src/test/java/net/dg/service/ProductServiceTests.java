package net.dg.service;

import net.dg.entity.Product;
import net.dg.repository.ProductRepository;
import net.dg.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    private ProductRepository productRepository;

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void shouldReturnAllProducts() {

        Product expected1 = Product.builder().id(1L).name("Laptop ASUS TUF")
                .price(BigDecimal.valueOf(1000)).build();
        Product expected2 = Product.builder().id(2L).name("Iphone XR")
                .price(BigDecimal.valueOf(1100.)).build();

        List<Product> products = List.of(expected1, expected2);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> productList = productService.getAllProducts();

        assertEquals(2, productList.size());
        verify(productRepository, times(1)).findAll();
    }


    @Test
    void shouldReturnProductBasedOnId() {
        Product expected = Product.builder().id(1L).name("Laptop")
                .price(BigDecimal.valueOf(100)).build();

        Mockito.when(productRepository.findById(expected.getId()))
                .thenReturn(Optional.of(expected));

        Optional<Product> optional = productService.getProductById(expected.getId());
        Product actual = optional.orElse(null);
        System.out.println(expected.getId());

        assertNotNull(expected);
        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteProductBasedOnId() {
        Product expected = Product.builder().id(1L).name("Laptop")
                .price(BigDecimal.valueOf(100)).build();

        productService.deleteProductById(expected.getId());
        Mockito.verify(productRepository).deleteById(expected.getId());
    }

}
