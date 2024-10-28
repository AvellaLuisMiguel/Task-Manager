package task_manager_back.task_manager_back.dto;

import lombok.Data;

@Data
public class TokenDto {
    private String token;

    public TokenDto(String token) {
        this.token = token;
    }
}
