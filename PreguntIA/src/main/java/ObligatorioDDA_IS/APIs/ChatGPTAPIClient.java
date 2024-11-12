package ObligatorioDDA_IS.APIs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ChatGPTAPIClient {

    @Value("${chatgpt.api.key}")
    private String apiKey;

    private final String apiUrl = "https://api.openai.com/v1/completions";

    public String sendRequest(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        // Crear el cuerpo de la solicitud
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "text-davinci-003");
        requestBody.put("prompt", prompt);
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
                return (String) choices.get(0).get("text");
            } else {
                throw new RuntimeException("Error al obtener respuesta de la API de ChatGPT");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error en la solicitud a la API de ChatGPT", e);
        }
    }
}
