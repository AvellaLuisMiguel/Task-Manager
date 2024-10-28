package task_manager_back.task_manager_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public User getUserByEmail(String email) {
         
        User user= userRepository.findByEmail(email);
        if (user==null){
            throw new UserExceptions("USER_DONT_FOUND");
        }
        return user;
    }

    @Transactional
    public List<Task> getTasks(String userEmaString) {
        User user = userRepository.findByEmail(userEmaString);
        return user.getTasks();
    }

}
