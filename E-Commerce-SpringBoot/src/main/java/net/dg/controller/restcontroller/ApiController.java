package net.dg.controller.restcontroller;

import lombok.AllArgsConstructor;
import net.dg.model.User;
import net.dg.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;

    @GetMapping(value = "/users")
    public List<User> getAllUsers(){
        return userService.findAll();
    }

    @DeleteMapping(path = "/deleteuser/{id}")
    public void deletePerson(@PathVariable Long id){
        userService.deleteById(id);
    }

    @GetMapping(value = "/getuser/{id}")
    public Optional<User> getUserById(@PathVariable("id") Long id){

            return userService.findById(id);
    }

}
