package org.vitalup.vitalup.controller.Profile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.Profile.BasicDetailsDTO;
import org.vitalup.vitalup.service.Interface.ProfileInterface;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileInterface profileService;

    @PostMapping("/basicProfile")
    public ResponseEntity<ApiResponse<?>> basicProfileUpdate(BasicDetailsDTO request){
        ApiResponse<?> response = profileService.updatebasicDetails(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
