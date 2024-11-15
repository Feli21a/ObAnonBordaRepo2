package ObligatorioDDA_IS.APIs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ChatGPTAPIClient {
    
    private String apiKey;

    private final String apiUrl = "https://api.openai.com/v1/chat/completions";

    public String sendRequest(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        // Crear el cuerpo de la solicitud en formato JSON
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");

        // Crear mensajes en formato de chat
        List<Map<String, String>> messages = List.of(
                Map.of("role", "user", "content", prompt));
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 100);
        requestBody.put("temperature", 0.7);

        // Configurar los encabezados
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");

                // Acceder al contenido de "message" en la primera opción
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            } else {
                System.err.println(
                        "Error en la respuesta de la API: " + response.getStatusCode() + " - " + response.getBody());
                throw new RuntimeException("Error en la respuesta de la API: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error al realizar la solicitud a la API de ChatGPT: " + e.getMessage());
            throw new RuntimeException("Error en la solicitud a la API de ChatGPT", e);
        }
    }
}
