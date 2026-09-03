package fr.florobart.pmt.modules.usersProjects;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users-projects")
public class UserProjectController {

    private final UserProjectService service;

    public UserProjectController(UserProjectService userProjectService) {
        this.service = userProjectService;
    }

    @GetMapping("/users/{userId}")
    public List<UserProject> getByUserId(@PathVariable Long userId) {
        return service.getByUserId(userId);
    }

    @GetMapping("/projects/{projectId}")
    public List<UserProject> getByProjectId(@PathVariable Long projectId) {
        return service.getByProjectId(projectId);
    }

    @GetMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<UserProject> getByUserIdAndProjectId(
            @PathVariable Long userId,
            @PathVariable Long projectId) {
        return ResponseEntity.ok(service.getByUserIdAndProjectId(userId, projectId));
    }

    @PostMapping
    public ResponseEntity<UserProject> create(
            @RequestParam(required = false) String email,
            @RequestBody UserProject userProject) {
        UserProject created = service.create(email, userProject);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<UserProject> update(
            @PathVariable Long userId,
            @PathVariable Long projectId,
            @RequestBody UserProject userProject) {
        return ResponseEntity.ok(service.update(userId, projectId, userProject));
    }

    @DeleteMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long userId,
            @PathVariable Long projectId) {
        service.delete(userId, projectId);
        return ResponseEntity.noContent().build();
    }
}