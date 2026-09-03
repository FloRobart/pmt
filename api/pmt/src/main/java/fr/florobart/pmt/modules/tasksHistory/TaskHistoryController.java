package fr.florobart.pmt.modules.tasksHistory;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks-history")
public class TaskHistoryController {
    private final TaskHistoryRepository repository;

    public TaskHistoryController(TaskHistoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/tasks/{taskId}")
    public List<TaskHistory> getByTaskId(@PathVariable Long taskId) {
        return repository.findByTask_IdOrderByIdDesc(taskId);
    }
}