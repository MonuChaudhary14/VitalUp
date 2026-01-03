package org.vitalup.vitalup.controller.foodScanner;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vitalup.vitalup.dto.foodScanner.FoodAnalysisResponse;
import org.vitalup.vitalup.service.foodScanner.GeminiFoodScannerService;

@RestController
@RequestMapping("/api/v1/food-scan")
@RequiredArgsConstructor
public class FoodScanController {

	private final GeminiFoodScannerService geminiService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<FoodAnalysisResponse> scanFood(@RequestParam("image") MultipartFile image) {

		if (image.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		FoodAnalysisResponse analysis = geminiService.analyzeFoodImage(image);

		return ResponseEntity.ok(analysis);
	}
}