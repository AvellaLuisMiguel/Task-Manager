package task_manager_back.task_manager_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import task_manager_back.task_manager_back.dto.UserCreateDto;
import task_manager_back.task_manager_back.exception.AuthExceptions;
import task_manager_back.task_manager_back.exception.UserExceptions;
import task_manager_back.task_manager_back.model.*;
import task_manager_back.task_manager_back.repository.UserRepository;


import java.util.List;
import javax.transaction.Transactional;

@Service
public class UserService {

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
    public User getUserByEmail(String email) {
        User user= userRepository.findByEmail(email);
        if (user==null){
            throw new UserExceptions("USER_DONT_FOUND");
        }
        return user;
    }

    @Transactional
    public List<Task> getTasks(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserExceptions("USER_DONT_FOUND" + userId));
        return user.getTasks();
    }
}
