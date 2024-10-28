package task_manager_back.task_manager_back.data;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import task_manager_back.task_manager_back.model.StateTask;
import task_manager_back.task_manager_back.repository.StateTaskRepository;

@Component
public class DataLoader implements CommandLineRunner {
     @Autowired
    private StateTaskRepository stateTaskRepository;

    @Override
    public void run(String... args) throws Exception {
        StateTask task1 = new StateTask();
        task1.setName("Pending");

        StateTask task2 = new StateTask();
        task2.setName("Progress");

        StateTask task3 = new StateTask();
        task3.setName("Completed");

        stateTaskRepository.saveAll(Arrays.asList(task1, task2, task3));
    }
}
