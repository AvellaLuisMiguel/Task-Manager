package task_manager_back.task_manager_back.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import task_manager_back.task_manager_back.model.StateTask;
import task_manager_back.task_manager_back.repository.StateTaskRepository;

@Service
public class StateTaskService {
    
    @Autowired
    private StateTaskRepository stateTaskRepository;

    @Transactional
    public List<StateTask> getAllStateTasks() {
        return stateTaskRepository.findAll();
    }

}
