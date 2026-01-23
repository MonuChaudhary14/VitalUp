package org.vitalup.vitalup.controller.Health.waterIntake;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.health.waterIntake.AddWaterRequestDTO;
import org.vitalup.vitalup.dto.health.waterIntake.WaterIntakeResponseDTO;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.service.Interface.Health.WaterIntakeInterface;

@RestController
@RequestMapping("/api/v1/water")
@RequiredArgsConstructor
public class WaterController {

    private final WaterIntakeInterface waterService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<WaterIntakeResponseDTO>> addWater(
            @Valid @RequestBody AddWaterRequestDTO request,
            Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        ApiResponse<WaterIntakeResponseDTO> response = waterService.addWater(user, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<WaterIntakeResponseDTO>> today(Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        ApiResponse<WaterIntakeResponseDTO> response = waterService.getToday(user);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
