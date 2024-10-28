package task_manager_back.task_manager_back.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import task_manager_back.task_manager_back.dto.TaskDto;
import task_manager_back.task_manager_back.exception.TaskExceptions;
import task_manager_back.task_manager_back.exception.UserExceptions;
import task_manager_back.task_manager_back.model.StateTask;
import task_manager_back.task_manager_back.model.Task;
import task_manager_back.task_manager_back.model.User;
import task_manager_back.task_manager_back.repository.StateTaskRepository;
import task_manager_back.task_manager_back.repository.TaskRepository;
import task_manager_back.task_manager_back.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StateTaskRepository stateTaskRepository;

    private TaskDto taskDto;
    private User user;
    private StateTask stateTask;

    @BeforeEach
    public void setUp() {
        taskDto = new TaskDto();
        taskDto.setDescription("Test Task");
        taskDto.setDate("01/11/2024");

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        
        stateTask = new StateTask();
        stateTask.setName("Pending");
    }

    @Test
    public void testCreateNewTask_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(stateTaskRepository.findByName("Pending")).thenReturn(stateTask);

        Task createdTask = taskService.createNewTask(taskDto, 1L);

        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getDescription());
        assertEquals(LocalDateTime.parse("2024-11-01T00:00"), createdTask.getDeadline());
        assertEquals(user, createdTask.getUser());
        assertEquals(stateTask, createdTask.getState());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void testCreateNewTask_InvalidDateFormat() {
        taskDto.setDate("invalid-date");

        TaskExceptions thrown = assertThrows(TaskExceptions.class, () -> {
            taskService.createNewTask(taskDto, 1L);
        });

        assertEquals("INVALID_DATE_FORMAT", thrown.getMessage());
    }

    @Test
    public void testCreateNewTask_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserExceptions thrown = assertThrows(UserExceptions.class, () -> {
            taskService.createNewTask(taskDto, 1L);
        });

        assertEquals("USER_NOT_FOUND: 1", thrown.getMessage());
    }

    @Test
    public void testDeleteTask_Success() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    public void testDeleteTask_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        TaskExceptions thrown = assertThrows(TaskExceptions.class, () -> {
            taskService.deleteTask(1L);
        });

        assertEquals("TASK_NOT_FOUND1", thrown.getMessage());
    }


    @Test
    public void testUpdateTaskState_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        TaskExceptions thrown = assertThrows(TaskExceptions.class, () -> {
            taskService.updateTaskState(1L, "Pending");
        });

        assertEquals("TASK_NOT_FOUND1", thrown.getMessage());
    }

    @Test
    public void testUpdateTaskState_InvalidStateName() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(stateTaskRepository.findByName("InvalidState")).thenReturn(null);

        TaskExceptions thrown = assertThrows(TaskExceptions.class, () -> {
            taskService.updateTaskState(1L, "InvalidState");
        });

        assertEquals("STATE_NAME_INVALID", thrown.getMessage());
    }
}
