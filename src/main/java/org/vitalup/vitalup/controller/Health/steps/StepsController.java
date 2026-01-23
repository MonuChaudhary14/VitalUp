package org.vitalup.vitalup.controller.Health.steps;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.health.steps.AddStepsRequestDTO;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.service.Interface.Health.StepsInterface;
import org.vitalup.vitalup.features.steps.dto.StepsResponseDTO;


@RestController
@RequestMapping("/api/v1/steps")
@RequiredArgsConstructor
public class StepsController {

    private final StepsInterface stepsService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<StepsResponseDTO>> addSteps(
            @Valid @RequestBody AddStepsRequestDTO request, Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        ApiResponse<StepsResponseDTO> response = stepsService.addSteps(user, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<StepsResponseDTO>> today(Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        ApiResponse<StepsResponseDTO> response = stepsService.getToday(user);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
