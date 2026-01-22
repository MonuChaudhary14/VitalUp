package org.vitalup.vitalup.service.ProfileService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.Profile.BasicDetailsDTO;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.entities.Profile.UserHealthProfile;
import org.vitalup.vitalup.repository.Auth.userRepository;
import org.vitalup.vitalup.repository.Profile.ProfileRepository;
import org.vitalup.vitalup.security.UsernameValidator;
import org.vitalup.vitalup.service.Interface.ProfileInterface;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class ProfileService implements ProfileInterface {

    private final ProfileRepository profileRepository;
    private final UsernameValidator usernameValidator;
    private final userRepository userRepo;

    ProfileService(ProfileRepository profileRepository, UsernameValidator usernameValidator, userRepository userRepo){
        this.profileRepository = profileRepository;
        this.usernameValidator = usernameValidator;
        this.userRepo = userRepo;
    }

    @Transactional
    public ApiResponse<?> updatebasicDetails(BasicDetailsDTO request){

        Users user = (Users) Objects.requireNonNull(SecurityContextHolder.getContext()
					.getAuthentication()).getPrincipal();

        UserHealthProfile profile = profileRepository.findByUserId(Objects.requireNonNull(user).getId())
                .orElseThrow(() -> new RuntimeException("Health profile not found"));

        if(request.getUsername() != null && !request.getUsername().isBlank()){

            String newUsername = request.getUsername().trim();

            if(!usernameValidator.checkUsername(newUsername)){
                return new ApiResponse<>(400,"Invalid username format",null);
            }

            if(!newUsername.equalsIgnoreCase(user.getUsername()) && userRepo.existsByUsername(newUsername)){
                return new ApiResponse<>(409,"Username already taken",null);
            }

            user.setUsername(newUsername);
        }

        if(request.getFullName() != null && !request.getFullName().isBlank()){
            profile.setFullName(request.getFullName().trim());
        }

        if(request.getDob() != null){
            try {
                LocalDate dob = LocalDate.parse(request.getDob());
                if (dob.isAfter(LocalDate.now())) {
                    return new ApiResponse<>(400,"Date of birth cannot be in the future",null);
                }
                profile.setDateOfBirth(dob);
            }
            catch(Exception e){
                return new ApiResponse<>(400,"Invalid DOB format (yyyy-MM-dd)",null);
            }
        }

        if(request.getGender() != null){
            profile.setGender(request.getGender());
        }

        if(request.getHeightCm() != null){
            if (request.getHeightCm() < 50 || request.getHeightCm() > 250){
                return new ApiResponse<>(400,"Invalid height value",null);
            }
            profile.setHeightCm(request.getHeightCm());
        }

        if(request.getWeightKg() != null){
            if(request.getWeightKg() < 10 || request.getWeightKg() > 300){
                return new ApiResponse<>(400,"Invalid weight value",null);
            }
            profile.setWeightKg(request.getWeightKg());
        }

        if(request.getBloodGroup() != null){
            profile.setBloodGroup(request.getBloodGroup());
        }

        if(request.getRestingHeartRate() != null){
            if(request.getRestingHeartRate() < 30 || request.getRestingHeartRate() > 220){
                return new ApiResponse<>(400,"Invalid resting heart rate",null);
            }
            profile.setRestingHeartRate(request.getRestingHeartRate());
        }

        if(request.getSmoker() != null){
            profile.setSmoker(request.getSmoker());
        }

        if(request.getAlcoholConsumer() != null){
            profile.setAlcoholConsumer(request.getAlcoholConsumer());
        }

        if(request.getActivityLevel() != null && !request.getActivityLevel().isBlank()){
            profile.setActivityLevel(request.getActivityLevel().trim().toLowerCase());
        }

        if(request.getEmergencyContactName() != null && !request.getEmergencyContactName().isBlank()){
            profile.setEmergencyContactName(request.getEmergencyContactName().trim());
        }

        if(request.getEmergencyContactPhone() != null && !request.getEmergencyContactPhone().isBlank()){
            profile.setEmergencyContactPhone(request.getEmergencyContactPhone().trim());
        }

        if(request.getOrganDonor() != null){
            profile.setOrganDonor(request.getOrganDonor());
        }

        userRepo.save(user);
        profileRepository.save(profile);

        return new ApiResponse<>(200,"Profile updated successfully",null);
    }

}
