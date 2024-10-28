package task_manager_back.task_manager_back.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import task_manager_back.task_manager_back.dto.TaskDto;
import task_manager_back.task_manager_back.exception.TaskExceptions;
import task_manager_back.task_manager_back.model.StateTask;
import task_manager_back.task_manager_back.model.Task;
import task_manager_back.task_manager_back.model.User;
import task_manager_back.task_manager_back.repository.StateTaskRepository;
import task_manager_back.task_manager_back.repository.TaskRepository;
import task_manager_back.task_manager_back.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StateTaskRepository stateTaskRepository;

    private User user;
    private StateTask pendingState;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
    
        user = new User();
        user.setEmail("test@example.com");
        
        pendingState = new StateTask();
        pendingState.setId(1L);
        pendingState.setName("Pending");
    }

    @Test
    void testCreateNewTask_Success() {
        TaskDto taskDto = new TaskDto();
        taskDto.setDescription("Test task");
        taskDto.setDate("01/01/2024");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(stateTaskRepository.findByName("Pending")).thenReturn(pendingState);

        Task createdTask = taskService.createNewTask(taskDto, "test@example.com");

        assertNotNull(createdTask);
        assertEquals("Test task", createdTask.getDescription());
        assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0), createdTask.getDeadline());
        assertEquals(pendingState, createdTask.getState());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testCreateNewTask_InvalidDateFormat() {
        TaskDto taskDto = new TaskDto();
        taskDto.setDescription("Test task");
        taskDto.setDate("invalid-date");

        Exception exception = assertThrows(TaskExceptions.class, () -> {
            taskService.createNewTask(taskDto, "test@example.com");
        });

        assertEquals("INVALID_DATE_FORMAT", exception.getMessage());
    }

    @Test
    void testUpdateTaskState_Success() {
        Task task = new Task();
        task.setId(1L);
        task.setUser(user);
        task.setState(pendingState);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        StateTask completedState = new StateTask();
        completedState.setId(2L);
        completedState.setName("Completed");
        when(stateTaskRepository.findByName("Completed")).thenReturn(completedState);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Task updatedTask = taskService.updateTaskState(1L, "Completed");
        assertNotNull(updatedTask);
        assertEquals("Completed", updatedTask.getState().getName());
        verify(taskRepository, times(1)).save(task);
    }
    

    @Test
    void testUpdateTaskState_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.updateTaskState(1L, "Completed");
        });

        assertEquals("Tarea no encontrada: 1", exception.getMessage());
    }

    @Test
    void testUpdateTaskState_InvalidState() {
        Task task = new Task();
        task.setId(1L);
        task.setUser(user);
        task.setState(pendingState);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(stateTaskRepository.findByName("InvalidState")).thenReturn(null);

        Exception exception = assertThrows(TaskExceptions.class, () -> {
            taskService.updateTaskState(1L, "InvalidState");
        });

        assertEquals("STATE_NAME_INVALID", exception.getMessage());
    }
}
