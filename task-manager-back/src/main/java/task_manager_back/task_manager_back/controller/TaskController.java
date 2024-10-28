package task_manager_back.task_manager_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import task_manager_back.task_manager_back.dto.TaskDto;
import task_manager_back.task_manager_back.model.Task;
import task_manager_back.task_manager_back.service.TaskService;




@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    
    @PostMapping("/")
    public ResponseEntity<?> createTask(@RequestBody TaskDto task) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Task newTask = taskService.createNewTask(task,email);
            return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{taskId}/state/{stateName}")
    public ResponseEntity<?> putMethodName(@PathVariable Long taskId, @PathVariable String stateName) {
        try {
            Task taskUpdate = taskService.updateTaskState(taskId,stateName);
            return ResponseEntity.status(HttpStatus.CREATED).body(taskUpdate);
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
}
