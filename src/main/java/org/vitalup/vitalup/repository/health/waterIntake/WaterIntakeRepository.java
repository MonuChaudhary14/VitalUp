package org.vitalup.vitalup.repository.health.waterIntake;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.entities.health.waterIntake.WaterIntake;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface WaterIntakeRepository extends JpaRepository<WaterIntake, UUID> {

    Optional<WaterIntake> findByUserAndDate(Users user, LocalDate date);
}