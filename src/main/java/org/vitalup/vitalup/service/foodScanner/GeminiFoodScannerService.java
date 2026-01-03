package org.vitalup.vitalup.service.foodScanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.vitalup.vitalup.dto.foodScanner.FoodAnalysisResponse;
import org.vitalup.vitalup.dto.gemini.GeminiRequest;
import org.vitalup.vitalup.dto.gemini.GeminiResponse;

import java.net.URI;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiFoodScannerService {

	@Value("${gemini.api.key}")
	private String apiKey;

	@Value("${gemini.api.url}")
	private String apiUrl;

	private final RestClient restClient = RestClient.create();
	private final ObjectMapper objectMapper = new ObjectMapper();

	private static final String PROMPT_TEXT = """
        Identify this food item from the image.
        Estimate its nutritional content for a typical single serving.
        If uncertain, make conservative estimates.

        Return ONLY a JSON object with this exact schema:
        {
            "foodName": "String",
            "confidence": 0.0,
            "nutrition": {
                "calories": 0.0,
                "macros": {
                    "protein": 0.0,
                    "carbs": 0.0,
                    "fat": 0.0,
                    "dietaryFiber": 0.0,
                    "sugar": 0.0,
                    "saturatedFat": 0.0
                }
            }
        }
        Do not include explanations or markdown.
        """;

	public FoodAnalysisResponse analyzeFoodImage(MultipartFile imageFile) {
		try {
			String base64Image = Base64.getEncoder()
				.encodeToString(imageFile.getBytes());

			GeminiRequest request = GeminiRequest.builder()
				.contents(List.of(
					GeminiRequest.Content.builder()
						.parts(List.of(
							GeminiRequest.Part.builder()
								.text(PROMPT_TEXT)
								.build(),
							GeminiRequest.Part.builder()
								.inlineData(GeminiRequest.InlineData.builder()
									.mimeType(imageFile.getContentType())
									.data(base64Image)
									.build())
								.build()
						))
						.build()
				))
				.generationConfig(GeminiRequest.GenerationConfig.builder()
					.responseMimeType("application/json")
					.build())
				.build();

			String finalUrl = apiUrl + "?key=" + apiKey;

			URI uri = URI.create(finalUrl);

			GeminiResponse response = restClient.post()
				.uri(String.valueOf(uri))
				.contentType(MediaType.APPLICATION_JSON)
				.body(request)
				.retrieve()
				.body(GeminiResponse.class);

			if (response == null || response.getCandidates().isEmpty()) {
				throw new RuntimeException("Empty response from Gemini");
			}

			String rawText = response.getCandidates().getFirst()
				.getContent().getParts().getFirst().getText();

			String cleanJson = extractPureJson(rawText);
			FoodAnalysisResponse result =
				objectMapper.readValue(cleanJson, FoodAnalysisResponse.class);

			result.getNutrition().setEstimated(true);
			return result;

		} catch (Exception e) {
			throw new RuntimeException("Food scan failed", e);
		}
	}

	private String extractPureJson(String text) {
		int start = text.indexOf("{");
		int end = text.lastIndexOf("}");
		if (start == -1 || end == -1) {
			throw new RuntimeException("Invalid JSON format");
		}
		return text.substring(start, end + 1);
	}
}