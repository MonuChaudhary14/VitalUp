package org.vitalup.vitalup.entities.Profile;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.entities.Profile.Enums.ActivityLevel;
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
    private Double bloodPressureUpper;
    private Double bloodPressureLower;
    private Double heightCm;
    private Double weightKg;
    private BloodGroup bloodGroup;
    private Integer restingHeartRate;
    private Double oxygenLevel = 97.5;
    private Boolean smoker;
    private Boolean alcoholConsumer;
    private Double sleepingHours;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    private String healthCondition;
    private String emergencyContactName;
    private String emergencyContactPhone;

    @Column(nullable = false)
    private Boolean organDonor;

    private String regularMedicine;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String allergies;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (organDonor == null) {
            organDonor = false;
        }
        if (oxygenLevel == null) {
            oxygenLevel = 97.5;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
