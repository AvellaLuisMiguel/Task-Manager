package task_manager_back.task_manager_back.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import task_manager_back.task_manager_back.model.*;
import task_manager_back.task_manager_back.service.UserService;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
  
    @GetMapping("/task/")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> getUserById() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            List<Task> newUser = userService.getTasks(email);
            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}