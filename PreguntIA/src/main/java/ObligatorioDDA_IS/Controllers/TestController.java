package ObligatorioDDA_IS.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ObligatorioDDA_IS.APIs.ChatGPTAPIClient;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final ChatGPTAPIClient chatGPTAPIClient;

    @Autowired
    public TestController(ChatGPTAPIClient chatGPTAPIClient) {
        this.chatGPTAPIClient = chatGPTAPIClient;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> testChatGPT() {
        String prompt = "Genera una pregunta trivia sobre historia con cuatro opciones.";
        try {
            String response = chatGPTAPIClient.sendRequest(prompt);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la solicitud: " + e.getMessage());
        }
    }
}
