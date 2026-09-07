package fr.florobart.pmt.modules.users;

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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock UserRepository repository;
    @InjectMocks UserService service;

    @Test
    void delegatesReads() {
        User user = new User();
        when(repository.findAll()).thenReturn(List.of(user));
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.findByEmail("a@b.fr")).thenReturn(user);
        assertEquals(List.of(user), service.getAll());
        assertSame(user, service.getById(1L));
        assertSame(user, service.getByEmail("a@b.fr"));
    }

    @Test
    void handlesMissingUserAndLogin() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(1L));
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("a@b.fr");
        request.setPassword("bad");
        when(repository.findByEmailAndPassword("a@b.fr", "bad")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> service.login(request));
    }

    @Test
    void createsUpdatesAndLogsIn() {
        User user = new User();
        user.setId(8L);
        when(repository.save(user)).thenReturn(user);
        assertSame(user, service.create(user));
        assertEquals(null, user.getId());
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("a@b.fr");
        request.setPassword("good");
        when(repository.findByEmailAndPassword("a@b.fr", "good")).thenReturn(user);
        assertSame(user, service.login(request));
        User details = new User();
        details.setUsername("new");
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        service.update(1L, details);
        assertEquals("new", user.getUsername());
    }

    @Test
    void deletesExistingAndRejectsMissingUser() {
        when(repository.existsById(1L)).thenReturn(true);
        service.delete(1L);
        verify(repository).deleteById(1L);
        when(repository.existsById(2L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> service.delete(2L));
    }
}