package org.vitalup.vitalup.dto.Profile;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.vitalup.vitalup.entities.Profile.Enums.BloodGroup;
import org.vitalup.vitalup.entities.Profile.Enums.Gender;

@Data
@NoArgsConstructor
public class BasicDetailsDTO {

    private String username;
    private String fullName;
    private String dob;
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
}
