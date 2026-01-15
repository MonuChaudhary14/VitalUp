package org.vitalup.vitalup.service.pushNotification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.pushNotification.RegisterDeviceRequestDTO;
import org.vitalup.vitalup.dto.pushNotification.SendNotificationRequestDTO;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.entities.pushNotification.DeviceToken;
import org.vitalup.vitalup.repository.pushNotification.DeviceTokenRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final DeviceTokenRepository deviceTokenRepository;

    public ApiResponse<String> registerDevice(Users user, RegisterDeviceRequestDTO request){

        if (request == null ||
                request.getToken() == null || request.getToken().isBlank()) {
            return new ApiResponse<>(400, "Invalid device token", null);
        }

        deviceTokenRepository.findByToken(request.getToken())
                .ifPresentOrElse(existing -> {
                            existing.setUser(user);
                            existing.setPlatform(request.getPlatform());
                            deviceTokenRepository.save(existing);
                        },
                        () -> {
                            DeviceToken token = new DeviceToken();
                            token.setUser(user);
                            token.setToken(request.getToken());
                            token.setPlatform(request.getPlatform());
                            deviceTokenRepository.save(token);
                        }
                );

        return new ApiResponse<>(200,"Device registered successfully",null);
    }

    public ApiResponse<String> sendNotificationToUser(Users user, SendNotificationRequestDTO request){
        if (request == null || request.getTitle() == null || request.getBody() == null) {
            return new ApiResponse<>(400, "Invalid notification data", null);
        }

        List<DeviceToken> devices = deviceTokenRepository.findAllByUser(user);

        if(devices.isEmpty()){
            return new ApiResponse<>(404, "No registered devices found", null);
        }

        try{
            for (DeviceToken device : devices) {
                Message message = Message.builder()
                        .setToken(device.getToken())
                        .putData("title", request.getTitle())
                        .putData("body", request.getBody())
                        .build();

                FirebaseMessaging.getInstance().send(message);
            }

            return new ApiResponse<>(200,"Notification sent successfully",null);
        }
        catch (Exception e) {
            return new ApiResponse<>(500,"Failed to send notification",null);
        }
    }

}