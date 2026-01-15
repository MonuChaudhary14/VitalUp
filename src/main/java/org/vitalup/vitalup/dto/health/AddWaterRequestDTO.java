package org.vitalup.vitalup.dto.health;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddWaterRequestDTO {

    @NotNull
    @Min(1)
    private Integer amountMl;
}