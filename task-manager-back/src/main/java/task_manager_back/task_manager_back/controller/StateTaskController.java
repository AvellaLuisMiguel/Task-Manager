package task_manager_back.task_manager_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task_manager_back.task_manager_back.model.StateTask;
import task_manager_back.task_manager_back.service.StateTaskService;

import java.util.List;

@RestController
@RequestMapping("/state-tasks")
public class StateTaskController {

    @Autowired
    private StateTaskService stateTaskService;

    @GetMapping
    public ResponseEntity<List<StateTask>> getAllStateTasks() {
        List<StateTask> stateTasks = stateTaskService.getAllStateTasks();
        return ResponseEntity.ok(stateTasks);
    }
}
