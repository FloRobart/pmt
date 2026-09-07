package fr.florobart.pmt.modules.projects;

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
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.florobart.pmt.modules.usersProjects.Role;
import fr.florobart.pmt.modules.usersProjects.UserProject;
import fr.florobart.pmt.modules.usersProjects.UserProjectId;
import fr.florobart.pmt.modules.usersProjects.UserProjectService;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock ProjectRepository repository;
    @Mock UserProjectService userProjectService;
    @InjectMocks ProjectService service;

    @Test
    void delegatesReadsAndFindsProjects() {
        Project project = new Project();
        when(repository.findAll()).thenReturn(List.of(project));
        when(repository.findById(1L)).thenReturn(Optional.of(project));

        assertEquals(List.of(project), service.getAll());
        assertSame(project, service.getById(1L));
    }

    @Test
    void throwsWhenProjectDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getById(1L));
    }

    @Test
    void createsProjectAndAddsAdministrator() {
        Project project = new Project();
        project.setId(99L);
        when(repository.save(project)).thenAnswer(invocation -> {
            project.setId(99L);
            return project;
        });

        Project result = service.create(7L, project);

        assertSame(project, result);
        ArgumentCaptor<UserProject> captor = ArgumentCaptor.forClass(UserProject.class);
        verify(userProjectService).create(org.mockito.ArgumentMatchers.eq(null), captor.capture());
        assertEquals(Role.ADMINISTRATEUR, captor.getValue().getRole());
        assertEquals(new UserProjectId(7L, 99L), captor.getValue().getId());
        verify(repository).save(project);
    }

    @Test
    void updatesFieldsAndDeletesExistingProject() {
        Project existing = new Project();
        Project details = new Project();
        Date startDate = new Date();
        details.setName("Updated");
        details.setDescription("Description");
        details.setStartDate(startDate);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        when(repository.existsById(1L)).thenReturn(true);

        assertSame(existing, service.update(1L, details));
        assertEquals("Updated", existing.getName());
        assertEquals("Description", existing.getDescription());
        assertEquals(startDate, existing.getStartDate());
        service.delete(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void refusesToDeleteMissingProject() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.delete(1L));
    }
}