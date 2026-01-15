package org.vitalup.vitalup.controller.Health.sleep;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.health.sleep.AddSleepRequestDTO;
import org.vitalup.vitalup.dto.health.sleep.SleepResponseDTO;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.service.health.sleep.SleepService;

@RestController
@RequestMapping("/api/v1/sleep")
@RequiredArgsConstructor
public class SleepController {

    private final SleepService sleepService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<SleepResponseDTO>> addSleep(@Valid @RequestBody AddSleepRequestDTO request,
                                                                  Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        ApiResponse<SleepResponseDTO> response = sleepService.addSleep(user, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<SleepResponseDTO>> today(Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        ApiResponse<SleepResponseDTO> response = sleepService.getToday(user);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}