package org.vitalup.vitalup.repository.health.sleep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.entities.health.sleep.SleepRecord;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface SleepRepository extends JpaRepository<SleepRecord, UUID> {

    Optional<SleepRecord> findByUserAndDate(Users user, LocalDate date);
}