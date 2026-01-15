package org.vitalup.vitalup.service.health;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.health.AddWaterRequestDTO;
import org.vitalup.vitalup.dto.health.WaterIntakeResponseDTO;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.entities.health.WaterIntake;
import org.vitalup.vitalup.repository.health.WaterIntakeRepository;
import org.vitalup.vitalup.service.Interface.Health.WaterIntakeInterface;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class waterIntakeService implements WaterIntakeInterface {

    private final WaterIntakeRepository repository;

    public ApiResponse<WaterIntakeResponseDTO> addWater(Users user, AddWaterRequestDTO request){

        if(request == null || request.getAmountMl() == null){
            return new ApiResponse<>(400, "Invalid request", null);
        }

        LocalDate today = LocalDate.now();

        WaterIntake intake = repository
                .findByUserAndDate(user, today)
                .orElseGet(() -> {
                    WaterIntake water = new WaterIntake();
                    water.setUser(user);
                    water.setDate(today);
                    water.setConsumedMl(0);
                    return water;
                });

        intake.setConsumedMl(intake.getConsumedMl() + request.getAmountMl());
        repository.save(intake);

        return new ApiResponse<>(200,"Water intake updated",mapToDto(intake));
    }

    public ApiResponse<WaterIntakeResponseDTO> getToday(Users user){

        WaterIntake intake = repository.findByUserAndDate(user, LocalDate.now()).orElse(null);

        if(intake == null){
            return new ApiResponse<>(200,"No water intake found",null);
        }

        return new ApiResponse<>(200,"Today's water intake",mapToDto(intake));
    }

    private WaterIntakeResponseDTO mapToDto(WaterIntake intake){
        int progress = Math.min(100, (intake.getConsumedMl() * 100) / intake.getDailyGoalMl());

        return new WaterIntakeResponseDTO(
                intake.getId(),
                intake.getDate(),
                intake.getConsumedMl(),
                intake.getDailyGoalMl(),
                progress
        );
    }
}