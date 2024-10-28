package task_manager_back.task_manager_back.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import task_manager_back.task_manager_back.dto.UserCreateDto;
import task_manager_back.task_manager_back.model.*;

import task_manager_back.task_manager_back.service.UserService;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<?> postMethodName(@RequestBody UserCreateDto userCreateDto) {
        try {
            User newUser = userService.createUser(userCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
  
    @GetMapping("/task/{idUser}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> getTaskUserById(@PathVariable  Long idUser) {
        try {
            List<Task> newUser = userService.getTasks(idUser);
            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}