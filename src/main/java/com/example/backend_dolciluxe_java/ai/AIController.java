package com.example.backend_dolciluxe_java.ai;
import java.util.Map;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/claid")
public class AIController {

    @Autowired
    private AIService aiService;
    @Autowired
    private Cloudinary cloudinary;


    @PostMapping("/edit")
    public String editImageClaidForDesign(@RequestParam("file") MultipartFile file) {
        try {
            ResponseEntity<String> response = aiService.editImageClaidForDesign(file);
            ObjectMapper responseMapper = new ObjectMapper();
            JsonNode responseJson = responseMapper.readTree(response.getBody());
            JsonNode fileUrlNode = responseJson.get("data").get("output").get("tmp_url");
            String fileUrl = fileUrlNode.asText();
            return fileUrl;
        } catch(JsonProcessingException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/gererate")
    public String generateImageClaid(@RequestBody String message) {
        try {
            ResponseEntity<String> response = aiService.generateImageClaid(message);
            ObjectMapper responseMapper = new ObjectMapper();
            JsonNode responseJson = responseMapper.readTree(response.getBody());
            JsonNode outputArray = responseJson.get("data").get("output");
            JsonNode firstOutput = outputArray.get(0);
            String fileUrl = firstOutput.get("tmp_url").asText();
            return fileUrl;
        } catch(JsonProcessingException e) {
            return e.getMessage();
        }
    }
    @PostMapping("/gererate-admin")
    public String generateImageClaidToAdmin(@RequestBody String message) {
        try {
            ResponseEntity<String> response = aiService.generateImageClaidToAdmin(message);
            ObjectMapper responseMapper = new ObjectMapper();
            JsonNode responseJson = responseMapper.readTree(response.getBody());
            JsonNode outputArray = responseJson.get("data").get("output");
            JsonNode firstOutput = outputArray.get(0);
            String fileUrl = firstOutput.get("tmp_url").asText();
            return fileUrl;
        } catch(JsonProcessingException e) {
            return e.getMessage();
        }
    }
    
    @PostMapping("/cloudinary")
    public ResponseEntity<?> uploadFromUrl(@RequestParam("imageUrl") String imageUrl) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(imageUrl, ObjectUtils.emptyMap());

            return ResponseEntity.ok(Map.of(
                "url", uploadResult.get("secure_url"),
                "public_id", uploadResult.get("public_id")
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload from URL failed: " + e.getMessage());
        }
    }
}

