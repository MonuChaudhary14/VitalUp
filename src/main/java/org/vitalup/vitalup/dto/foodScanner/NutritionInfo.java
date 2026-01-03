package org.vitalup.vitalup.dto.foodScanner;

import lombok.Data;

@Data
public class NutritionInfo {
	private double calories;
	private Macros macros;
	private boolean estimated;
}
