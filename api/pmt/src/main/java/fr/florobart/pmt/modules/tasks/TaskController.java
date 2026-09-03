package fr.florobart.pmt.modules.tasks;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    /**
     * Constructor for ClientController
     * @param taskService The service injected by Spring
     */
    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/projects/{id}")
    public List<Task> getByProjectId(@PathVariable("id") Long projectId) {
        return service.getByProjectId(projectId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/projects/{projectId}/status/{status}")
    public List<Task> getByProjectIdAndStatus(
            @PathVariable Long projectId, @PathVariable TaskStatus status) {
        return service.getByProjectIdAndStatus(projectId, status);
    }

    @PostMapping("/projects/{id}")
    public ResponseEntity<Task> create(@PathVariable Long id, @RequestBody Task task) {
        Task createdTask = service.create(id, task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task task) {
        Task updatedTask = service.update(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestBody Task task) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
