package task_manager_back.task_manager_back.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task_manager_back.task_manager_back.dto.UserLoginDto;
import task_manager_back.task_manager_back.exception.AuthExceptions;
import task_manager_back.task_manager_back.model.User;
import task_manager_back.task_manager_back.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    private UserLoginDto userLoginDto;

    @BeforeEach
    public void setUp() {
        userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("test@example.com");
        userLoginDto.setPassword("password");
    }

    @Test
    public void testLoginUser_Success() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail(userLoginDto.getEmail())).thenReturn(user);

        String userId = authService.loginUser(userLoginDto);

        assertEquals("1", userId);
        verify(userRepository).findByEmail(userLoginDto.getEmail());
    }

    @Test
    public void testLoginUser_UserNotFound() {
        when(userRepository.findByEmail(userLoginDto.getEmail())).thenReturn(null);

        AuthExceptions thrown = assertThrows(AuthExceptions.class, () -> {
            authService.loginUser(userLoginDto);
        });

        assertEquals("INVALID_CREDENTIALS", thrown.getMessage());
        verify(userRepository).findByEmail(userLoginDto.getEmail());
    }

    @Test
    public void testLoginUser_InvalidPassword() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("wrongPassword");

        when(userRepository.findByEmail(userLoginDto.getEmail())).thenReturn(user);

        AuthExceptions thrown = assertThrows(AuthExceptions.class, () -> {
            authService.loginUser(userLoginDto);
        });

        assertEquals("INVALID_CREDENTIALS", thrown.getMessage());
        verify(userRepository).findByEmail(userLoginDto.getEmail());
    }
}
