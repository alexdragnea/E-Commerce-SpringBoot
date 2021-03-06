package net.dg.service.impl;

import lombok.AllArgsConstructor;
import net.dg.entity.ShippingAddress;
import net.dg.entity.Cart;
import net.dg.entity.User;
import net.dg.repository.UserRepository;
import net.dg.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService  {

    private final UserRepository userRepository;


    @Override
    public boolean saveNewUser(User user) {
        Optional<User> userFromDb = userRepository.findByEmail(user.getEmail());

        if (userFromDb.isPresent())
            return false;

        final String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setCart(new Cart());
        user.setAddress(new ShippingAddress());
        userRepository.save(user);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user " + email + " was not found"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void delete(User user) {
        if (user.getRole().name().equals("ADMIN"))
            return;
        userRepository.delete(user);
    }

    @Override
    public void blockUser(Long userId) {
        Optional<User> userFromDB = userRepository.findById(userId);
        User user = userFromDB.orElseThrow(() -> new UsernameNotFoundException("user with id " + userId + "was not found"));
        if (user.getRole().name().equals("ADMIN"))
            return;
        user.setAccountLocked();
        userRepository.save(user);
    }

    @Override
    public void unblockUser(Long userId) {
        Optional<User> userFromDB = userRepository.findById(userId);
        User user = userFromDB.orElseThrow(() -> new UsernameNotFoundException("user with id " + userId + "was not found"));
        user.setAccountUnLocked();
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user, String firstName, String lastName, String password) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(new BCryptPasswordEncoder().encode(password));

        userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateAddress(User user, String streetName, String streetNumber,
                              String city, String contact, String zipCode) {

        user.setAddress(streetName, streetNumber, city, contact, zipCode);

        userRepository.save(user);
    }

}
