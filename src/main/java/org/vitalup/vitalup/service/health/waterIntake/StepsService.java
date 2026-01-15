package org.vitalup.vitalup.service.health.waterIntake;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.health.steps.AddStepsRequestDTO;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.entities.health.StepsActivity;
import org.vitalup.vitalup.repository.health.steps.StepsRepository;
import org.vitalup.vitalup.features.steps.dto.StepsResponseDTO;
import org.vitalup.vitalup.service.Interface.Health.StepsInterface;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StepsService implements StepsInterface {

    private final StepsRepository repository;

    public ApiResponse<StepsResponseDTO> addSteps(Users user,
            AddStepsRequestDTO request){

        if (request == null || request.getSteps() == null || request.getDistanceKm() == null) {
            return new ApiResponse<>(400, "Invalid request", null);
        }

        LocalDate today = LocalDate.now();

        StepsActivity activity = repository
                .findByUserAndDate(user, today)
                .orElseGet(() -> {
                    StepsActivity s = new StepsActivity();
                    s.setUser(user);
                    s.setDate(today);
                    s.setSteps(0);
                    s.setDistanceKm(0);
                    return s;
                });

        activity.setSteps(activity.getSteps() + request.getSteps());
        activity.setDistanceKm(activity.getDistanceKm() + request.getDistanceKm());
        activity.setActivityLevel(calculateLevel(activity.getSteps()));

        repository.save(activity);

        return new ApiResponse<>(200,"Steps updated",mapToDto(activity));
    }

    public ApiResponse<StepsResponseDTO> getToday(Users user){

        StepsActivity activity = repository.findByUserAndDate(user, LocalDate.now())
                .orElse(null);

        if(activity == null){
            return new ApiResponse<>(200,"No steps data for today",null);
        }

        return new ApiResponse<>(200,"Today's steps",mapToDto(activity));
    }

    private String calculateLevel(int steps){
        if(steps < 3000) return "LOW";
        if(steps < 8000) return "MEDIUM";
        return "HIGH";
    }

    private StepsResponseDTO mapToDto(StepsActivity activity) {
        return new StepsResponseDTO(
                activity.getId(),
                activity.getDate(),
                activity.getSteps(),
                activity.getDistanceKm(),
                activity.getActivityLevel()
        );
    }

}
