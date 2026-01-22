package org.vitalup.vitalup.entities.Profile;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.entities.Profile.Enums.BloodGroup;
import org.vitalup.vitalup.entities.Profile.Enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "user_health_profiles")
public class UserHealthProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;

    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;

    private Double heightCm;
    private Double weightKg;
    private BloodGroup bloodGroup;
    private Integer restingHeartRate;

    private Boolean smoker;
    private Boolean alcoholConsumer;

    private String activityLevel;

    private String emergencyContactName;
    private String emergencyContactPhone;
    private Boolean organDonor;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
