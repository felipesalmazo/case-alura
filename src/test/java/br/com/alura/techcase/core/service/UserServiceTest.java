package br.com.alura.techcase.core.service;

import br.com.alura.techcase.api.dto.user.CreateUserForm;
import br.com.alura.techcase.api.model.User;
import br.com.alura.techcase.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private CreateUserForm form;

    @BeforeEach
    void setUp() {
        form = new CreateUserForm("usertest", "user test", "email@email.com", "ADMIN", "12345");
        user = new User(form, form.password());
    }

    @Test
    void shouldGetUserByUserName() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Optional<User> userOptional = userService.getUserByUserName(user.getUsername());

        assertEquals(Optional.of(user), userOptional);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldNotGetUserByUserName() {
        when(userRepository.findByUsername("any")).thenReturn(Optional.empty());

        Optional<User> userOptional = userService.getUserByUserName("any");

        assertFalse(userOptional.isPresent());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldCreateUser() {
        when(userRepository.save(user)).thenReturn(user);

        var userCreated = userService.createUser(form);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        assertEquals(userArgumentCaptor.getValue(), userCreated);

    }
}