package fr.florobart.pmt.modules.projects;

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
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    /**
     * Constructor for ClientController
     * @param projectService The service injected by Spring
     */
    public ProjectController(ProjectService projectService) {
        this.service = projectService;
    }

    @GetMapping
    public List<Project> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Project> create(@PathVariable Long userId, @RequestBody Project project) {
        Project createdProject = service.create(userId, project);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable Long id, @RequestBody Project project) {
        Project updatedProject = service.update(id, project);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestBody Project project) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
