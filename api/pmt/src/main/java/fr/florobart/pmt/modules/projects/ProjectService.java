package fr.florobart.pmt.modules.projects;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.florobart.pmt.modules.usersProjects.Role;
import fr.florobart.pmt.modules.usersProjects.UserProject;
import fr.florobart.pmt.modules.usersProjects.UserProjectId;
import fr.florobart.pmt.modules.usersProjects.UserProjectService;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final UserProjectService userProjectService;

    public ProjectService(ProjectRepository projectRepository, UserProjectService userProjectService) {
        this.repository = projectRepository;
        this.userProjectService = userProjectService;
    }

    public List<Project> getAll() {
        return repository.findAll();
    }

    public Project getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project introuvable avec l'ID : " + id));
    }

    public Project create(Long userId, Project project) {
        project.setId(null);
        project = repository.save(project);

        // Add the user as a member of the project with the role "ADMINISTRATEUR"
        UserProject userProject = new UserProject();
        userProject.setId(new UserProjectId(userId, project.getId()) );
        userProject.setRole(Role.ADMINISTRATEUR);

        this.userProjectService.create(null, userProject);

        return project;
    }

    public Project update(Long id, Project projectDetails) {
        Project project = getById(id);
        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());
        project.setStartDate(projectDetails.getStartDate());
        return repository.save(project);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Project introuvable avec l'ID : " + id);
        }
        repository.deleteById(id);
    }
}
