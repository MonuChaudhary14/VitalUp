package org.vitalup.vitalup.entities.health.sleep;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vitalup.vitalup.entities.Auth.Users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "sleep_records",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"})
)
@Data
@NoArgsConstructor
public class SleepRecord {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private Users user;

    private LocalDate date;

    private LocalDateTime sleepStart;
    private LocalDateTime wakeUp;

    private int totalMinutes;
}