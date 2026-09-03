package fr.florobart.pmt.modules.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Find a user by email and password
    User findByEmailAndPassword(String email, String password);
    // Find a user by email
    User findByEmail(String email);
}