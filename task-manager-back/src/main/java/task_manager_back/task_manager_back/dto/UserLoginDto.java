package task_manager_back.task_manager_back.dto;

import lombok.Data;

@Data
public class UserLoginDto {
    private String email;
    private String password;
}