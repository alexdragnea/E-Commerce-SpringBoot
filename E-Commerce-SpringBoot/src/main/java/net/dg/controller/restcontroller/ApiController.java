package net.dg.controller.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.dg.entity.Order;
import net.dg.entity.OrderedProduct;
import net.dg.entity.Product;
import net.dg.entity.User;
import net.dg.exceptions.ProductNotFoundException;
import net.dg.repository.OrderRepository;
import net.dg.service.OrderService;
import net.dg.service.ProductService;
import net.dg.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @GetMapping(value = "/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }


    @PostMapping("/product")
    public ResponseEntity<Void> addProduct(@RequestBody Product product) {
        productService.saveProduct(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/user")
    public ResponseEntity<Void> addUser(@Valid @RequestBody User user) {
        userService.saveNewUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(path = "/user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@PathVariable Long id, @RequestBody Map<String, Object> requestBody) {

        requestBody.put("id", id);
        ObjectMapper objectMapper = new ObjectMapper();
        User userToBeUpdated = objectMapper.convertValue(requestBody, User.class);
        userService.saveNewUser(userToBeUpdated);
    }

    @GetMapping(value = "/products/{keyword}")
    public List<Product> getProductsBykeyword(@PathVariable String keyword) throws ProductNotFoundException {

        return productService.findByKeyword(keyword);
    }

    @DeleteMapping(path = "/deleteuser/{id}")
    public void deletePerson(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @DeleteMapping(path = "/deleteproduct/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {

        Optional<User> optional = userService.findById(id);
        if (optional.isPresent()) {
            User userById = optional.get();
            return new ResponseEntity<>(userById, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {

        Optional<Product> optional = productService.getProductById(id);
        if (optional.isPresent()) {
            Product productById = optional.get();
            return new ResponseEntity<>(productById, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/orderedproducts/{id}")
    public Set<OrderedProduct> getAllOrders(@PathVariable("id") Long id) {

        return orderService.findAllOrderedProductByOrderId(id);
    }


    @GetMapping(value = "/page/{pageNum}")
    public Page<Product> getPageOfProducts(@PathVariable("pageNum") int pageNum) {

        return productService.listAll(pageNum);
    }

    @GetMapping(value = "/orders")
    public Set<Order> getUserCart() {

        return orderService.findAll();
    }

    @PatchMapping(value = "/blockuser/{id}")
    public void blockUser(@PathVariable("id") Long id) {

        userService.blockUser(id);
    }

    @PatchMapping(value = "/unblockuser/{id}")
    public void unBlockUser(@PathVariable("id") Long id) {

        userService.unblockUser(id);
    }

}
