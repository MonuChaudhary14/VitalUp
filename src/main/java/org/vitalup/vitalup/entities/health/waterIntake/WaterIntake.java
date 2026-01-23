package org.vitalup.vitalup.entities.health.waterIntake;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vitalup.vitalup.entities.Auth.Users;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "water_intake",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"})
)
@Data
@NoArgsConstructor
public class WaterIntake {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional= false)
    @JoinColumn(name = "user_id")
    private Users user;
    private LocalDate date;
    private int consumedMl;

    private int dailyGoalMl = 3000;

}
