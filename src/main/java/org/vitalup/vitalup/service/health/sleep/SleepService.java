package org.vitalup.vitalup.service.health.sleep;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.health.sleep.AddSleepRequestDTO;
import org.vitalup.vitalup.dto.health.sleep.SleepResponseDTO;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.entities.health.sleep.SleepRecord;
import org.vitalup.vitalup.repository.health.sleep.SleepRepository;

import java.time.Duration;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SleepService {

    private final SleepRepository repository;

    public ApiResponse<SleepResponseDTO> addSleep(Users user, AddSleepRequestDTO request){

        if(request == null || request.getSleepStart() == null ||
                request.getWakeUp() == null || request.getWakeUp().isBefore(request.getSleepStart())){
            return new ApiResponse<>(400,"Invalid sleep timings",null);
        }

        LocalDate date = request.getSleepStart().toLocalDate();

        int durationMinutes = (int) Duration
                .between(request.getSleepStart(), request.getWakeUp()).toMinutes();

        SleepRecord record = repository.findByUserAndDate(user, date)
                .orElseGet(() -> {
                    SleepRecord s = new SleepRecord();
                    s.setUser(user);
                    s.setDate(date);
                    return s;
                });

        record.setSleepStart(request.getSleepStart());
        record.setWakeUp(request.getWakeUp());
        record.setTotalMinutes(durationMinutes);

        repository.save(record);

        return new ApiResponse<>(200,"Sleep data recorded",mapToDto(record));
    }

    public ApiResponse<SleepResponseDTO> getToday(Users user){

        SleepRecord record = repository
                .findByUserAndDate(user, LocalDate.now()).orElse(null);

        if (record == null) {
            return new ApiResponse<>(200,"No sleep data for today",null);
        }

        return new ApiResponse<>(200,"Today's sleep data", mapToDto(record));
    }

    private SleepResponseDTO mapToDto(SleepRecord record){
        return new SleepResponseDTO(
                record.getId(),
                record.getDate(),
                record.getSleepStart(),
                record.getWakeUp(),
                record.getTotalMinutes()
        );
    }

}
