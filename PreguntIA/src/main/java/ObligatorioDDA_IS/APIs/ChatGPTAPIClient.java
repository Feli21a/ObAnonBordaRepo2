package ObligatorioDDA_IS.APIs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ChatGPTAPIClient {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ChatGPTAPIClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendRequest(String prompt) throws JSONException {
        String url = "https://api.openai.com/v1/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "text-davinci-003");
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", 100);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
