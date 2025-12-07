package org.vitalup.vitalup.repository.Auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vitalup.vitalup.entities.Auth.Users;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface userRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByEmail(String email);

}
