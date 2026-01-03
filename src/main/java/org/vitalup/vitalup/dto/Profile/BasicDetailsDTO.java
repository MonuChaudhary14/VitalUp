package org.vitalup.vitalup.dto.Profile;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.vitalup.vitalup.entities.Profile.Enums.BloodGroup;
import org.vitalup.vitalup.entities.Profile.Enums.Gender;

@Data
@NoArgsConstructor
public class BasicDetailsDTO {

    // Users table
    private String username;

    // Profile

    private String fullName;
    private String dob;
    private Gender gender;

    // Physical

    private Double heightCm;
    private Double weightKg;

    // Vital

    private BloodGroup bloodGroup;
    private Integer restingHeartRate;

    private Boolean smoker;
    private Boolean alcoholConsumer;

    // Activity

    private String activityLevel;

    // Emergency

    private String emergencyContactName;
    private String emergencyContactPhone;
    private Boolean organDonor;
}
