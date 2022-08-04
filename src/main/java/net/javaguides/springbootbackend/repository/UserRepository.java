package net.javaguides.springbootbackend.repository;

import net.javaguides.springbootbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);
    Optional<User> findByUsername(String username);
}
