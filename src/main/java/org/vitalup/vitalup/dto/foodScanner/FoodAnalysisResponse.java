package org.vitalup.vitalup.dto.foodScanner;

import lombok.Data;

@Data
public class FoodAnalysisResponse {
	private String foodName;
	private double confidence;
	private NutritionInfo nutrition;
}
