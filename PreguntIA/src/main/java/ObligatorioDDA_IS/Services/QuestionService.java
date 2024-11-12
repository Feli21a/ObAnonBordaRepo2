package ObligatorioDDA_IS.Services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.APIs.ChatGPTAPIClient;
import ObligatorioDDA_IS.Models.Question;

@Service
public class QuestionService {

    private final ChatGPTAPIClient apiClient;

    @Autowired
    public QuestionService(ChatGPTAPIClient apiClient) {
        this.apiClient = apiClient;
    }

    public Question fetchQuestion(String category, String difficulty) throws JSONException {
        String prompt = generatePrompt(category, difficulty);
        String response = apiClient.sendRequest(prompt);
        return parseQuestion(response);
    }

    private String generatePrompt(String category, String difficulty) {
        return "Generate a trivia question for a " + difficulty + " level in the " + category + " category. " +
                "Include 4 answer options and specify the correct answer in a JSON format like this: " +
                "{\"question\": \"Your question?\", \"options\": [\"option1\", \"option2\", \"option3\", \"option4\"], \"answer\": \"correct option\"}";
    }

    private Question parseQuestion(String response) throws JSONException {
        // Parse JSON response from ChatGPT
        JSONObject json = new JSONObject(response);
        String questionText = json.getString("question");
        String correctAnswer = json.getString("answer");
        String[] optionsArray = json.getJSONArray("options").join(",").split(",");
        return new Question(questionText, Arrays.asList(optionsArray), correctAnswer);
    }
}
