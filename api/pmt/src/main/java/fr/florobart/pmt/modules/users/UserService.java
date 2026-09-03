package fr.florobart.pmt.modules.users;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User introuvable avec l'ID : " + id));
    }

    public User getByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User create(User user) {
        user.setId(null);
        return repository.save(user);
    }

    public User login(UserLoginRequest userLoginRequest) {
        User user = repository.findByEmailAndPassword(userLoginRequest.getEmail(), userLoginRequest.getPassword());
        if (user == null) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }
        return user;
    }

    public User update(Long id, User userDetails) {
        User user = getById(id);
        user.setUsername(userDetails.getUsername());
        return repository.save(user);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("User introuvable avec l'ID : " + id);
        }
        repository.deleteById(id);
    }
}
