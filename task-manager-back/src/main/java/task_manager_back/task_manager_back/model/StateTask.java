package task_manager_back.task_manager_back.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.util.List;

@Entity
@Data
public class StateTask {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; 

    @JsonIgnore
    @OneToMany(mappedBy = "state")
    private List<Task> tasks; 
}

