package fr.florobart.pmt.modules.tasks;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.florobart.pmt.modules.projects.Project;
import fr.florobart.pmt.modules.projects.ProjectService;
import fr.florobart.pmt.modules.tasksHistory.TaskHistory;
import fr.florobart.pmt.modules.tasksHistory.TaskHistoryRepository;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final ProjectService projectService;
    private final TaskHistoryRepository historyRepository;

    public TaskService(TaskRepository taskRepository, ProjectService projectService,
            TaskHistoryRepository historyRepository) {
        this.repository = taskRepository;
        this.projectService = projectService;
        this.historyRepository = historyRepository;
    }

    public List<Task> getByProjectId(Long projectId) {
        return repository.findByProject_Id(projectId);
    }

    public List<Task> getByProjectIdAndStatus(Long projectId, TaskStatus status) {
        return repository.findByProject_IdAndStatus(projectId, status);
    }

    public Task getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task introuvable avec l'ID : " + id));
    }

    public Task create(Long projectId, Task task) {
        task.setId(null);
        Project project = projectService.getById(projectId);
        task.setProject(project);
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }
        task = repository.save(task);
        return task;
    }

    public Task update(Long id, Task taskDetails) {
        Task task = getById(id);
        historyRepository.save(TaskHistory.from(task));
        task.setName(taskDetails.getName());
        task.setDescription(taskDetails.getDescription());
        task.setDueDate(taskDetails.getDueDate());
        task.setEndDate(taskDetails.getEndDate());
        task.setPriority(taskDetails.getPriority());
        if (taskDetails.getProject() != null) {
            task.setProject(taskDetails.getProject());
        }
        task.setAssignedTo(taskDetails.getAssignedTo());
        task.setStatus(taskDetails.getStatus() == null ? TaskStatus.TODO : taskDetails.getStatus());
        return repository.save(task);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Task introuvable avec l'ID : " + id);
        }
        repository.deleteById(id);
    }
}
