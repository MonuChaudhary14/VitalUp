package org.vitalup.vitalup.repository.health.steps;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.entities.health.StepsActivity;


import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface StepsRepository extends JpaRepository<StepsActivity, UUID> {

    Optional<StepsActivity> findByUserAndDate(Users user, LocalDate date);
}