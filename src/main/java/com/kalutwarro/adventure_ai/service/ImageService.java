package com.kalutwarro.adventure_ai.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.FileOutputStream;
import java.util.*;

@Service
public class ImageService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateImage(String prompt) {
        String url = "http://127.0.0.1:7860/sdapi/v1/txt2img";

        Map<String, Object> body = new HashMap<>();
        body.put("prompt", prompt);
        body.put("steps", 20);
        body.put("width", 512);
        body.put("height", 512);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        List<String> images = (List<String>) response.getBody().get("images");
        String base64Image = images.get(0);

        // Guardar en disco
        byte[] decoded = Base64.getDecoder().decode(base64Image);
        try (FileOutputStream fos = new FileOutputStream("generated.png")) {
            fos.write(decoded);
        } catch (Exception e) {
            throw new RuntimeException("Error guardando imagen", e);
        }

        return "generated.png"; // Podés devolver path local o URL si servís estáticos
    }
}
