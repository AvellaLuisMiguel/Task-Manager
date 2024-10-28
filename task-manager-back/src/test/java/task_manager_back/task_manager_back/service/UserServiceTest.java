package task_manager_back.task_manager_back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import task_manager_back.task_manager_back.exception.UserExceptions;
import task_manager_back.task_manager_back.model.Task;
import task_manager_back.task_manager_back.model.User;
import task_manager_back.task_manager_back.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        Task task = new Task();
        task.setDescription("Example Task");
        user.setTasks(Collections.singletonList(task));
    }

    @Test
    void testGetUserByEmail_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User result = userService.getUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test User", result.getName());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(null);

        UserExceptions exception = assertThrows(UserExceptions.class, () -> {
            userService.getUserByEmail("notfound@example.com");
        });

        assertEquals("USER_DONT_FOUND", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    void testGetTasks_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        List<Task> tasks = userService.getTasks("test@example.com");

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Example Task", tasks.get(0).getDescription());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

}
