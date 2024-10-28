package task_manager_back.task_manager_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import task_manager_back.task_manager_back.dto.TokenDto;
import task_manager_back.task_manager_back.dto.UserCreateDto;
import task_manager_back.task_manager_back.dto.UserLoginDto;
import task_manager_back.task_manager_back.model.*;
import task_manager_back.task_manager_back.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/")
    public ResponseEntity<?> postMethodName(@RequestBody UserCreateDto userCreateDto) {
        try {
            User newUser = authService.createUser(userCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto) {
        try {
            String token = authService.loginUser(userLoginDto);
            TokenDto tokenResponse = new TokenDto(token);
            return ResponseEntity.ok(tokenResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
}
