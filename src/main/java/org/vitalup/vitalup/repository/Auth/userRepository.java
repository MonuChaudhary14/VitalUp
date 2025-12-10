package org.vitalup.vitalup.repository.Auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vitalup.vitalup.entities.Auth.Users;

import java.util.Optional;
import java.util.UUID;

public interface userRepository extends JpaRepository<Users, UUID> {

	Optional<Users> findByEmail(String email);

	boolean existsByEmail(String email);

	Optional<Users> findByUsernameIgnoreCase(String username);

	boolean existsByUsername(String username);

}
