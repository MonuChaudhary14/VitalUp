package org.vitalup.vitalup.repository.Profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vitalup.vitalup.entities.Profile.UserHealthProfile;

import java.util.UUID;

public interface ProfileRepository extends JpaRepository<UserHealthProfile, UUID> {



}
