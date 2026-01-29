package org.vitalup.vitalup.dto.Profile;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.vitalup.vitalup.entities.Profile.Enums.ActivityLevel;
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
    private Double bloodPressureUpper;
    private Double bloodPressureLower;
    private Boolean smoker;
    private Boolean alcoholConsumer;
    private Double oxygenLevel;
    private ActivityLevel activityLevel;
    private Double sleepingHours;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private Boolean organDonor;
    private String healthCondition;
    private String regularMedicine;
    private String allergies;

}
