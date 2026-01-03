package org.vitalup.vitalup.service.Interface;

import org.springframework.stereotype.Service;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.Profile.BasicDetailsDTO;

@Service
public interface ProfileInterface {

    ApiResponse<?> updatebasicDetails(BasicDetailsDTO request);

}
