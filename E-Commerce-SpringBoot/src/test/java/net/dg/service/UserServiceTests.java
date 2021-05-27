package net.dg.service;

import net.dg.entity.User;
import net.dg.repository.UserRepository;
import net.dg.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import net.dg.entity.Address;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldReturnAllUsers(){
        User expected1 = User.builder().id(1L).firstName("Alex").email("alex@gmail.com")
                .password("alex").build();
        User expected2 = User.builder().id(2L).firstName("John")
                .password("alex").email("john@gmail.com").build();

        List<User> users = List.of(expected1, expected2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> userList = userService.findAll();

        assertEquals(2, userList.size());
        verify(userRepository, times(1)).findAll();

    }

    @Test
    void shouldReturnUserNotFoundException(){

        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("test@gmail.com"));
    }

}
