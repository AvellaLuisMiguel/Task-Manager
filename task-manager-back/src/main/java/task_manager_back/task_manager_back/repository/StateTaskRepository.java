package task_manager_back.task_manager_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import task_manager_back.task_manager_back.model.StateTask;

@Repository
public interface StateTaskRepository extends JpaRepository<StateTask, Long> {
    StateTask findByName(String name);
}
