package ObligatorioDDA_IS.Services;

import java.util.Arrays;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.APIs.ChatGPTAPIClient;
import ObligatorioDDA_IS.Models.Question;

@Service
public class QuestionService {

    private final ChatGPTAPIClient apiClient;
    private final String[] difficulties = { "Easy", "Medium", "Hard" };

    @Autowired
    public QuestionService(ChatGPTAPIClient apiClient) {
        this.apiClient = apiClient;
    }

    public Question fetchQuestion(String category, String difficulty) throws JSONException {
        String prompt = "Generate a " + difficulty + " level question in the category of " + category +
                " with 4 answer options, and indicate the correct answer.";

        String response = apiClient.sendRequest(prompt);
        return parseQuestion(response);
    }

    private Question parseQuestion(String response) throws JSONException {
        JSONObject json = new JSONObject(response);
        String questionText = json.getString("question");
        String correctAnswer = json.getString("answer");
        String[] optionsArray = json.getJSONArray("options").join(",").split(",");
        return new Question(questionText, Arrays.asList(optionsArray), correctAnswer);
    }

    public Question fetchMultiplayerQuestion(String category) throws JSONException {
        String difficulty = getRandomDifficulty();
        return fetchQuestion(category, difficulty);
    }

    private String getRandomDifficulty() {
        Random random = new Random();
        return difficulties[random.nextInt(difficulties.length)];
    }
}
