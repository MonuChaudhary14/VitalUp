package org.vitalup.vitalup.repository.pushNotification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.entities.pushNotification.DeviceToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, UUID>{

    Optional<DeviceToken> findByToken(String token);
    List<DeviceToken> findAllByUser(Users user);

}