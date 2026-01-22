package org.vitalup.vitalup.repository.Profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.entities.Profile.UserHealthProfile;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<UserHealthProfile, UUID> {

    Optional<UserHealthProfile> findByUserId(UUID id);

}
