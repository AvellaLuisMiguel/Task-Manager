package task_manager_back.task_manager_back.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import task_manager_back.task_manager_back.model.*;
import task_manager_back.task_manager_back.dto.UserCreateDto;
import task_manager_back.task_manager_back.dto.UserLoginDto;
import task_manager_back.task_manager_back.exception.AuthExceptions;
import task_manager_back.task_manager_back.repository.UserRepository;
import task_manager_back.task_manager_back.security.JwtUtil;



@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(UserCreateDto user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new AuthExceptions("EMAIL_DONT_AVALIABLE");
        }
        User userCreate=new User();
        userCreate.setName(user.getName());
        userCreate.setEmail(user.getEmail());
        userCreate.setPassword(user.getPassword());
        return userRepository.save(userCreate);
    }

    @Transactional
    public String loginUser(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail());
        if (user == null || !user.getPassword().equals(userLoginDto.getPassword())) {
            throw new AuthExceptions("INVALID_CREDENTIALS");
        }
        return "true";
    }
}
