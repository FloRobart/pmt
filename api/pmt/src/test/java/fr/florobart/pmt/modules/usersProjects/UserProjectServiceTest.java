package fr.florobart.pmt.modules.usersProjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.florobart.pmt.modules.users.User;
import fr.florobart.pmt.modules.users.UserService;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserProjectServiceTest {
    @Mock UserProjectRepository repository;
    @Mock UserService userService;
    @InjectMocks UserProjectService service;

    @Test
    void delegatesReadsAndFindsRelation() {
        UserProject relation = new UserProject(new UserProjectId(1L, 2L), Role.MEMBRE);
        when(repository.findAll()).thenReturn(List.of(relation));
        when(repository.findByIdUserId(1L)).thenReturn(List.of(relation));
        when(repository.findByIdProjectId(2L)).thenReturn(List.of(relation));
        when(repository.findById(new UserProjectId(1L, 2L))).thenReturn(Optional.of(relation));
        assertEquals(List.of(relation), service.getAll());
        assertEquals(List.of(relation), service.getByUserId(1L));
        assertEquals(List.of(relation), service.getByProjectId(2L));
        assertSame(relation, service.getByUserIdAndProjectId(1L, 2L));
    }

    @Test
    void createsWithAndWithoutEmail() {
        UserProject relation = new UserProject(new UserProjectId(null, 2L), Role.MEMBRE);
        User user = new User();
        user.setId(8L);
        when(userService.getByEmail("a@b.fr")).thenReturn(user);
        when(repository.save(relation)).thenReturn(relation);
        assertSame(relation, service.create("a@b.fr", relation));
        assertEquals(new UserProjectId(8L, 2L), relation.getId());
        service.create(null, relation);
        verify(repository, org.mockito.Mockito.times(2)).save(relation);
    }

    @Test
    void updatesAndDeletesRelations() {
        UserProject existing = new UserProject(new UserProjectId(1L, 2L), Role.MEMBRE);
        UserProject details = new UserProject(null, Role.ADMINISTRATEUR);
        when(repository.findById(new UserProjectId(1L, 2L))).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        assertSame(existing, service.update(1L, 2L, details));
        assertEquals(Role.ADMINISTRATEUR, existing.getRole());
        when(repository.existsById(new UserProjectId(1L, 2L))).thenReturn(true);
        service.delete(1L, 2L);
        verify(repository).deleteById(new UserProjectId(1L, 2L));
    }

    @Test
    void rejectsMissingRelations() {
        when(repository.findById(new UserProjectId(1L, 2L))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getByUserIdAndProjectId(1L, 2L));
        when(repository.existsById(new UserProjectId(1L, 2L))).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> service.delete(1L, 2L));
    }

    @Test
    void comparesCompositeIds() {
        UserProjectId id = new UserProjectId(1L, 2L);
        assertEquals(id, id);
        assertEquals(id, new UserProjectId(1L, 2L));
        assertEquals(id.hashCode(), new UserProjectId(1L, 2L).hashCode());
        assertEquals(false, id.equals(null));
        assertEquals(false, id.equals("id"));
    }
}