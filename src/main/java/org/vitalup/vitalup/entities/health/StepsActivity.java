package org.vitalup.vitalup.entities.health;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vitalup.vitalup.entities.Auth.Users;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "steps_activity",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"})
)
@Data
@NoArgsConstructor
public class StepsActivity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private Users user;

    private LocalDate date;
    private int steps;
    private double distanceKm;
    private String activityLevel;

}
