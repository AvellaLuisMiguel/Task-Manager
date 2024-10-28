package task_manager_back.task_manager_back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import task_manager_back.task_manager_back.dto.UserCreateDto;
import task_manager_back.task_manager_back.dto.UserLoginDto;
import task_manager_back.task_manager_back.exception.AuthExceptions;
import task_manager_back.task_manager_back.model.User;
import task_manager_back.task_manager_back.repository.UserRepository;
import task_manager_back.task_manager_back.security.JwtUtil;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_EmailAlreadyExists() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setEmail("test@example.com");
        userCreateDto.setName("Test User");
        userCreateDto.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(new User());

        AuthExceptions exception = assertThrows(AuthExceptions.class, () -> {
            authService.createUser(userCreateDto);
        });

        assertEquals("EMAIL_DONT_AVALIABLE", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateUser_Success() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setEmail("test@example.com");
        userCreateDto.setName("Test User");
        userCreateDto.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = authService.createUser(userCreateDto);

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testLoginUser_InvalidEmail() {
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("wrong@example.com");
        userLoginDto.setPassword("password");

        when(userRepository.findByEmail("wrong@example.com")).thenReturn(null);

        AuthExceptions exception = assertThrows(AuthExceptions.class, () -> {
            authService.loginUser(userLoginDto);
        });

        assertEquals("INVALID_CREDENTIALS", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("wrong@example.com");
    }

    @Test
    void testLoginUser_InvalidPassword() {
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("test@example.com");
        userLoginDto.setPassword("wrongpassword");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        AuthExceptions exception = assertThrows(AuthExceptions.class, () -> {
            authService.loginUser(userLoginDto);
        });

        assertEquals("INVALID_CREDENTIALS", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }
}
