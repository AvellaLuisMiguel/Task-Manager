package task_manager_back.task_manager_back.dto;

import lombok.Data;

@Data
public class TokenDto {
    private String id;

    public TokenDto(String token) {
        this.id = token;
    }
}
