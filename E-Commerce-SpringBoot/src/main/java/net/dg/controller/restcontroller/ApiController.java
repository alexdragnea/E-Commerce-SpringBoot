package net.dg.controller.restcontroller;

import lombok.AllArgsConstructor;
import net.dg.model.Order;
import net.dg.model.Product;
import net.dg.model.User;
import net.dg.service.OrderService;
import net.dg.service.ProductService;
import net.dg.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping(value = "/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }


    @PostMapping("/addproduct")
    public ResponseEntity<Void> addUser(@RequestBody Product product) {
        productService.saveProduct(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/deleteuser/{id}")
    public void deletePerson(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @GetMapping(value = "/getuser/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {

        Optional<User> optional = userService.findById(id);
        if (optional.isPresent()) {
            User userById = optional.get();
            return new ResponseEntity<>(userById, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/getproduct/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {

        Optional<Product> optional = productService.getProductById(id);
        if(optional.isPresent()){
            Product productById = optional.get();
            return new ResponseEntity<>(productById, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
