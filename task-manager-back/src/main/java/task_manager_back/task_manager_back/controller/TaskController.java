package task_manager_back.task_manager_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import task_manager_back.task_manager_back.dto.TaskDto;
import task_manager_back.task_manager_back.exception.TaskExceptions;
import task_manager_back.task_manager_back.model.Task;
import task_manager_back.task_manager_back.service.TaskService;




@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    
    @PostMapping("/{idUser}")
    public ResponseEntity<?> createTask(@PathVariable Long idUser,@RequestBody TaskDto task) {
        try {
            Task newTask=taskService.createNewTask(task, idUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId) {
        try {
            Task task = taskService.getTaskById(taskId);
            return ResponseEntity.ok(task);
        } catch (TaskExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @RequestBody TaskDto taskDto) {
        try {
            Task updatedTask = taskService.updateTask(taskId, taskDto);
            return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
