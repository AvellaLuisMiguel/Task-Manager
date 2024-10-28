package task_manager_back.task_manager_back.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import task_manager_back.task_manager_back.dto.TaskDto;
import task_manager_back.task_manager_back.exception.TaskExceptions;
import task_manager_back.task_manager_back.exception.UserExceptions;
import task_manager_back.task_manager_back.model.StateTask;
import task_manager_back.task_manager_back.model.Task;
import task_manager_back.task_manager_back.model.User;
import task_manager_back.task_manager_back.repository.StateTaskRepository;
import task_manager_back.task_manager_back.repository.TaskRepository;
import task_manager_back.task_manager_back.repository.UserRepository;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired StateTaskRepository stateTaskRepository;

    @Transactional
    public Task createNewTask(TaskDto task, Long userId){
        Task taskCreate = new Task();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate localDate = LocalDate.parse(task.getDate(), dateFormatter);
            LocalDateTime dateTime = localDate.atStartOfDay(); 
            taskCreate.setDeadline(dateTime);
        } catch (DateTimeParseException e) {
            throw new TaskExceptions("INVALID_DATE_FORMAT");
        }
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserExceptions("USER_NOT_FOUND: " + userId));

        taskCreate.setDescription(task.getDescription());
        taskCreate.setUser(user);
        StateTask pendingState = stateTaskRepository.findByName("Pending");
        taskCreate.setState(pendingState);
        this.taskRepository.save(taskCreate);
        
        return taskCreate;
    }

    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskExceptions("TASK_NOT_FOUND" + taskId));
        
        taskRepository.delete(task);
    }

    @Transactional
    public Task updateTaskState(Long taskId, String stateName) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskExceptions("TASK_NOT_FOUND" + taskId));

        StateTask newState = stateTaskRepository.findByName(stateName);
        if (newState==null){
            throw new TaskExceptions("STATE_NAME_INVALID");
        }

        task.setState(newState); 
        return taskRepository.save(task);
    }
}
