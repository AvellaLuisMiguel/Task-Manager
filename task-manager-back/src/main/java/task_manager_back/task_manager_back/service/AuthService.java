package task_manager_back.task_manager_back.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import task_manager_back.task_manager_back.model.*;
import task_manager_back.task_manager_back.dto.UserLoginDto;
import task_manager_back.task_manager_back.exception.AuthExceptions;
import task_manager_back.task_manager_back.repository.UserRepository;



@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    

    @Transactional
    public String loginUser(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail());
        if (user == null || !user.getPassword().equals(userLoginDto.getPassword())) {
            throw new AuthExceptions("INVALID_CREDENTIALS");
        }
        return user.getId().toString();
    }
}
