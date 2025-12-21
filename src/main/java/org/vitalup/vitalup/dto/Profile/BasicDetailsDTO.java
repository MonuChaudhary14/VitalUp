package org.vitalup.vitalup.dto.Profile;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.vitalup.vitalup.entities.Profile.BloodGroup;
import org.vitalup.vitalup.entities.Profile.Gender;

@Data
@NoArgsConstructor
public class BasicDetailsDTO {

    private String fullName;

    private String dob;

    private int age;

    private int weight;
    private int height;

    private Gender gender;

    private String profileImage;

    private BloodGroup bloodGroup;

    private int BloodPressure;

}
