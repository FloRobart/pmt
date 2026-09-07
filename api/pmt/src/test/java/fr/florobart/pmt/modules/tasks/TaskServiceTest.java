package fr.florobart.pmt.modules.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.florobart.pmt.modules.projects.Project;
import fr.florobart.pmt.modules.projects.ProjectService;
import fr.florobart.pmt.modules.tasksHistory.TaskHistoryRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock TaskRepository repository;
    @Mock ProjectService projectService;
    @Mock TaskHistoryRepository historyRepository;
    @InjectMocks TaskService service;

    @Test
    void delegatesTaskQueries() {
        Task task = new Task();
        when(repository.findByProject_Id(1L)).thenReturn(List.of(task));
        when(repository.findByProject_IdAndStatus(1L, TaskStatus.DONE)).thenReturn(List.of(task));

        assertEquals(List.of(task), service.getByProjectId(1L));
        assertEquals(List.of(task), service.getByProjectIdAndStatus(1L, TaskStatus.DONE));
    }

    @Test
    void findsTaskOrThrows() {
        Task task = new Task();
        when(repository.findById(1L)).thenReturn(Optional.of(task));
        assertSame(task, service.getById(1L));
        when(repository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(2L));
    }

    @Test
    void createsTaskWithDefaultStatusAndProject() {
        Project project = new Project();
        Task task = new Task();
        task.setId(10L);
        when(projectService.getById(1L)).thenReturn(project);
        when(repository.save(task)).thenReturn(task);

        service.create(1L, task);

        assertEquals(null, task.getId());
        assertSame(project, task.getProject());
        assertEquals(TaskStatus.TODO, task.getStatus());
        verify(repository).save(task);
    }

    @Test
    void updatesTaskAndKeepsOrReplacesProject() {
        Task existing = new Task();
        existing.setName("Old");
        existing.setStatus(TaskStatus.TODO);
        Task details = new Task();
        details.setName("New");
        details.setDescription("Desc");
        details.setDueDate(new Date());
        details.setEndDate(new Date());
        details.setPriority(2);
        details.setAssignedTo("user");
        details.setStatus(TaskStatus.DONE);
        Project project = new Project();
        details.setProject(project);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        service.update(1L, details);

        assertEquals("New", existing.getName());
        assertEquals(TaskStatus.DONE, existing.getStatus());
        assertSame(project, existing.getProject());
        verify(historyRepository).save(org.mockito.ArgumentMatchers.any());

        details.setProject(null);
        details.setStatus(null);
        service.update(1L, details);
        assertEquals(TaskStatus.TODO, existing.getStatus());
        assertSame(project, existing.getProject());
    }

    @Test
    void deletesExistingTaskAndRejectsMissingTask() {
        when(repository.existsById(1L)).thenReturn(true);
        service.delete(1L);
        verify(repository).deleteById(1L);
        when(repository.existsById(2L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> service.delete(2L));
    }
}