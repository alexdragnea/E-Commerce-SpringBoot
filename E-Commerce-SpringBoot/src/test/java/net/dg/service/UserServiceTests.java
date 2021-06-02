package net.dg.service;

import net.dg.entity.ShippingAddress;
import net.dg.entity.User;
import net.dg.repository.UserRepository;
import net.dg.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should return list of all users")
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
    @DisplayName("Should return UsernameNotFoundException.")
    void shouldReturnUserNotFoundException(){

        User user = User.builder().id(1L).firstName("Alexandru").lastName("Dragnea")
                .email("alexdg722@gmail.com").password("password").build();

        when(userService.loadUserByUsername("test@gmail.com")).thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("test@gmail.com"));
    }

    @Test
    void ShouldUpdateUserShippingAddress() {

        User user = User.builder().id(1L).firstName("Alexandru").lastName("Dragnea")
                .email("alexdg722@gmail.com").password("password").address(new ShippingAddress()).build();

        userService.updateAddress(user,"Memorandului", "19", "Brasov",
                "0725581974", "1991019");

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        User expectedUser = userArgumentCaptor.getValue();
        assertNotNull(expectedUser.getAddress());
        assertEquals("Memorandului", expectedUser.getAddress().getStreetName());
    }



}
