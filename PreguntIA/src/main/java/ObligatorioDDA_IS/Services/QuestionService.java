package ObligatorioDDA_IS.Services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.APIs.ChatGPTAPIClient;

@Service
public class QuestionService {

    private final ChatGPTAPIClient apiClient;
    private final String[] difficulties = { "Easy", "Medium", "Hard" };

    @Autowired
    public QuestionService(ChatGPTAPIClient apiClient) {
        this.apiClient = apiClient;
    }

    public String fetchQuestion(String category, String difficulty) throws JSONException {
        String prompt = "Generate a " + difficulty + " level question in the category of " + category +
                " with 4 answer options, and indicate the correct answer.";

        return apiClient.sendRequest(prompt);
    }

    public String fetchMultiplayerQuestion(String category) throws JSONException {
        // Seleccionar una dificultad aleatoria para el modo multijugador
        String difficulty = getRandomDifficulty();
        String prompt = "Generate a " + difficulty + " level question in the category of " + category +
                " with 4 answer options, and indicate the correct answer.";

        return apiClient.sendRequest(prompt);
    }

    private String getRandomDifficulty() {
        Random random = new Random();
        return difficulties[random.nextInt(difficulties.length)];
    }
}
