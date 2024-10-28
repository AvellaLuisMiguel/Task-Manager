package task_manager_back.task_manager_back.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import task_manager_back.task_manager_back.dto.UserCreateDto;
import task_manager_back.task_manager_back.exception.AuthExceptions;
import task_manager_back.task_manager_back.exception.UserExceptions;

import task_manager_back.task_manager_back.model.User;
import task_manager_back.task_manager_back.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private UserCreateDto userCreateDto;
    private User user;

    @BeforeEach
    public void setUp() {
        userCreateDto = new UserCreateDto();
        userCreateDto.setName("Test User");
        userCreateDto.setEmail("test@example.com");
        userCreateDto.setPassword("password");

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setTasks(Collections.emptyList());
    }

    @Test
    public void testCreateUser_Success() {
        when(userRepository.findByEmail(userCreateDto.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userCreateDto);

        assertNotNull(createdUser);
        assertEquals("Test User", createdUser.getName());
        assertEquals("test@example.com", createdUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testCreateUser_EmailAlreadyExists() {
        when(userRepository.findByEmail(userCreateDto.getEmail())).thenReturn(user);

        AuthExceptions thrown = assertThrows(AuthExceptions.class, () -> {
            userService.createUser(userCreateDto);
        });

        assertEquals("EMAIL_DONT_AVALIABLE", thrown.getMessage());
    }

    @Test
    public void testGetUserByEmail_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User foundUser = userService.getUserByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("Test User", foundUser.getName());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    public void testGetUserByEmail_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        UserExceptions thrown = assertThrows(UserExceptions.class, () -> {
            userService.getUserByEmail("nonexistent@example.com");
        });

        assertEquals("USER_DONT_FOUND", thrown.getMessage());
    }



    @Test
    public void testGetTasks_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserExceptions thrown = assertThrows(UserExceptions.class, () -> {
            userService.getTasks(1L);
        });

        assertEquals("USER_DONT_FOUND1", thrown.getMessage());
    }
}
