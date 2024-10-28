package task_manager_back.task_manager_back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import task_manager_back.task_manager_back.model.StateTask;
import task_manager_back.task_manager_back.repository.StateTaskRepository;

import java.util.Arrays;
import java.util.List;

public class StateTaskServiceTest {

    @InjectMocks
    private StateTaskService stateTaskService;

    @Mock
    private StateTaskRepository stateTaskRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStateTasks() {
        StateTask stateTask1 = new StateTask();

        stateTask1.setName("Pending");

        StateTask stateTask2 = new StateTask();
        stateTask2.setName("Completed");

        List<StateTask> mockStateTasks = Arrays.asList(stateTask1, stateTask2);

        when(stateTaskRepository.findAll()).thenReturn(mockStateTasks);

        List<StateTask> stateTasks = stateTaskService.getAllStateTasks();

        assertNotNull(stateTasks);
        assertEquals(2, stateTasks.size());
        assertEquals("Pending", stateTasks.get(0).getName());
        assertEquals("Completed", stateTasks.get(1).getName());
        verify(stateTaskRepository, times(1)).findAll();
    }
}
