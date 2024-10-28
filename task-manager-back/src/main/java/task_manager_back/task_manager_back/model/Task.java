package task_manager_back.task_manager_back.model;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column
    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = true)
    private StateTask state;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) 
    private User user;
}
