package fr.florobart.pmt.modules.usersProjects;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.florobart.pmt.modules.users.User;
import fr.florobart.pmt.modules.users.UserService;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserProjectService {

    private final UserProjectRepository repository;
    private final UserService userService;

    public UserProjectService(UserProjectRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public List<UserProject> getAll() {
        return repository.findAll();
    }

    public List<UserProject> getByUserId(Long userId) {
        return repository.findByIdUserId(userId);
    }

    public List<UserProject> getByProjectId(Long projectId) {
        return repository.findByIdProjectId(projectId);
    }

    public UserProject getByUserIdAndProjectId(Long userId, Long projectId) {
        return repository.findById(new UserProjectId(userId, projectId))
                .orElseThrow(() -> new EntityNotFoundException(
                "Relation introuvable pour l'utilisateur : " + userId + " et le projet : " + projectId));
    }

    public UserProject create(String email, UserProject userProject) {
        if (email != null) {
            User user = userService.getByEmail(email);
            Long projectId = (userProject.getId() != null) ? userProject.getId().getProjectId() : null;
            userProject.setId(new UserProjectId(user.getId(), projectId));
        }
        return repository.save(userProject);
    }

    public UserProject update(Long userId, Long projectId, UserProject userDetails) {
        UserProject existing = getByUserIdAndProjectId(userId, projectId);
        existing.setRole(userDetails.getRole());
        return repository.save(existing);
    }

    public void delete(Long userId, Long projectId) {
        UserProjectId id = new UserProjectId(userId, projectId);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Relation introuvable pour l'utilisateur : " + userId + " et le projet : " + projectId);
        }
        repository.deleteById(id);
    }
}
