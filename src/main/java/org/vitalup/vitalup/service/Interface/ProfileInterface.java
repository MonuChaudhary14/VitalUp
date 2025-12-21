package org.vitalup.vitalup.service.Interface;

import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.Profile.BasicDetailsDTO;

public interface ProfileInterface {

    ApiResponse<?> updatebasicDetails(BasicDetailsDTO request);

}
