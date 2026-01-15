package org.vitalup.vitalup.controller.pushNotification;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.pushNotification.RegisterDeviceRequestDTO;
import org.vitalup.vitalup.dto.pushNotification.SendNotificationRequestDTO;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.service.pushNotification.NotificationService;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/register-device")
    public ResponseEntity<ApiResponse<String>> registerDevice(@Valid @RequestBody RegisterDeviceRequestDTO request,
                                                              Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        ApiResponse<String> response = notificationService.registerDevice(user, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> sendNotification(
            @Valid @RequestBody SendNotificationRequestDTO request, Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        ApiResponse<String> response = notificationService.sendNotificationToUser(user, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}