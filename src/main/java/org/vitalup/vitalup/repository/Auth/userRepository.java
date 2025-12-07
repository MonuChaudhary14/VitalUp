package org.vitalup.vitalup.repository.Auth;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.vitalup.vitalup.entities.Auth.Users;

import java.util.Optional;

public interface userRepository extends MongoRepository<Users, String> {

    Optional<Users> findByEmail(String email);

}
