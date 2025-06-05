package com.example.backend_dolciluxe_java.ai;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AIService {
  // @Autowired
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${claid.upload.url}")
  private String API_URL;
  @Value("${claid.edit.url}")
  private String API_URL_2;
  @Value("${claid.generate.url}")
  private String API_URL_1;
  @Value("${claid.api.key}")
  private String API_KEY;
  @Value("${gemini.ask.url}")
  private String GEMINI_URL;
  @Value("${cloudinary.img.url}")
  private String IMG_URL;

    public ResponseEntity<String> uploadImageClaid(MultipartFile multipartFile){
    try {
      File tempFile = File.createTempFile("upload-", multipartFile.getOriginalFilename());
      multipartFile.transferTo(tempFile);

      Map<String, Object> operations = new HashMap<>();

      Map<String, Object> background = new HashMap<>();
      background.put("remove", false);
      operations.put("background", background);

      Map<String, Object> dataWrapper = new HashMap<>();
      dataWrapper.put("operations", operations);

      ObjectMapper objectMapper = new ObjectMapper();
      String jsonData = objectMapper.writeValueAsString(dataWrapper);

      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("file", new FileSystemResource(tempFile));

      HttpHeaders partHeaders = new HttpHeaders();
      partHeaders.setContentType(MediaType.APPLICATION_JSON);

      body.add("data", new HttpEntity<>(jsonData, partHeaders));

      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "Bearer " + API_KEY);
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
      ResponseEntity<String> response = restTemplate.postForEntity(API_URL, requestEntity, String.class);

      ObjectMapper responseMapper = new ObjectMapper();
      JsonNode responseJson = responseMapper.readTree(response.getBody());

      JsonNode fileUrlNode = responseJson.get("data").get("output").get("tmp_url");

      String fileUrl = fileUrlNode.asText();
      return ResponseEntity.ok(fileUrl);
    } catch(JsonProcessingException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    } catch(IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  public ResponseEntity<String> editImageClaidForDesign(MultipartFile multipartFile){
    ResponseEntity<String> uploadimageclaid = uploadImageClaid(multipartFile);
    if(uploadimageclaid.getStatusCode().is2xxSuccessful())
    {
      String fileUrl = uploadimageclaid.getBody();
      System.out.println("Đường dẫn ảnh đã upload: " + fileUrl);

      String message = "Convert this 2D cake image into a realistic 3D model.";
      Map<String, Object> operations = new HashMap<>();
      Map<String, Object> background = new HashMap<>();
      background.put("remove", false); 

      Map<String, Object> adjustments = new HashMap<>();
      adjustments.put("hdr", 1);
      adjustments.put("exposure", 10);
      adjustments.put("sharpness", 5);

      Map<String, Object> generative = new HashMap<>();
      generative.put("style_transfer", new HashMap<String, Object>() {{
          put("style_reference_image", IMG_URL);
          put("prompt", message);
          put("depth_strength", 1.0);
      }});

      operations.put("background", background);
      operations.put("adjustments", adjustments);
      operations.put("generative", generative);

      Map<String, Object> body = new HashMap<>();
      body.put("input", fileUrl);
      body.put("operations", operations);
      
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "Bearer " + API_KEY);
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
      ResponseEntity<String> response = restTemplate.postForEntity(API_URL_2, requestEntity, String.class);
      return response;
    }
    else {
      throw new IllegalStateException(uploadimageclaid.getBody());
    }
  }

  public ResponseEntity<String> generateImageClaid(String message) {
    ResponseEntity<String> sendtogeminitotranslate = sendToGeminiToTranslate(message);
    if(sendtogeminitotranslate.getStatusCode().is2xxSuccessful()) {
      String e_message = sendtogeminitotranslate.getBody();
      Map<String, Object> options = new HashMap<>();

      options.put("number_of_images", 1);
      options.put("guidance_scale", 5.0);


      Map<String, Object> body = new HashMap<>();
      body.put("input", e_message);
      body.put("options", options);
      
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "Bearer " + API_KEY);
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
      ResponseEntity<String> response = restTemplate.postForEntity(API_URL_1, requestEntity, String.class);
      return response;
    }
    else {
      throw new IllegalStateException(sendtogeminitotranslate.getBody());
    }   
  }

  public ResponseEntity<String> sendToGeminiToTranslate(String userInput) {
    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      String requestBody = buildRequestToTraslate(userInput);

      HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
      ResponseEntity<String> response = restTemplate.exchange(GEMINI_URL, HttpMethod.POST, request, String.class);

      ObjectMapper responseMapper = new ObjectMapper();
      JsonNode responseJson = responseMapper.readTree(response.getBody());

      JsonNode textNode = responseJson.get("candidates")
                    .get(0)
                    .get("content")
                    .get("parts")
                    .get(0)
                    .get("text");
      String textString = textNode.asText();
      return ResponseEntity.ok(textString);
    } catch (JsonProcessingException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  private String buildRequestToTraslate(String userInput) {
    String message = "Role: you're an experienced translator. Task: translate the user input to English. User input: '%s'. Response format: only the translation text.".formatted(userInput);
    return """
            {
              "contents": [
                {
                  "parts": [
                    {
                      "text": "%s"
                    }
                  ]
                }
              ]
            }
            """.formatted(message);
  }
  public ResponseEntity<String> generateImageClaidToAdmin(String message) {
    ResponseEntity<String> sendtogeminitoadmin = sendToGeminiToAdmin(message);
    if(sendtogeminitoadmin.getStatusCode().is2xxSuccessful()) {
      String top3_message = sendtogeminitoadmin.getBody();
      Map<String, Object> options = new HashMap<>();

      options.put("number_of_images", 1);
      options.put("guidance_scale", 5.0);


      Map<String, Object> body = new HashMap<>();
      body.put("input", top3_message);
      body.put("options", options);
      
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "Bearer " + API_KEY);
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
      ResponseEntity<String> response = restTemplate.postForEntity(API_URL_1, requestEntity, String.class);
      return response;
    }
    else {
      throw new IllegalStateException(sendtogeminitoadmin.getBody());
    }   
  }

  public ResponseEntity<String> sendToGeminiToAdmin(String adminInput){
    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      String requestBody = buildRequestToAdmin(adminInput);

      HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
      ResponseEntity<String> response = restTemplate.exchange(GEMINI_URL, HttpMethod.POST, request, String.class);

      ObjectMapper responseMapper = new ObjectMapper();
      JsonNode responseJson = responseMapper.readTree(response.getBody());

      JsonNode textNode = responseJson.get("candidates")
                    .get(0)
                    .get("content")
                    .get("parts")
                    .get(0)
                    .get("text");
      String textString = textNode.asText();
      return ResponseEntity.ok(textString);
    } catch (JsonProcessingException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  private String buildRequestToAdmin(String adminInput) {
    String message = "Role: you're a professional baker. Task: identify 4 common characteristics of the 3 types of cakes mentioned in the user input. User input: '%s'. Response format: only the English text describing the 4 characteristics.".formatted(adminInput);
    return """
            {
              "contents": [
                {
                  "parts": [
                    {
                      "text": "%s"
                    }
                  ]
                }
              ]
            }
            """.formatted(message);
  }

}
