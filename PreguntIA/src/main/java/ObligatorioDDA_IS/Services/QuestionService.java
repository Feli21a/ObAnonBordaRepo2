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
        String response;
        try {
            response = apiClient.sendRequest(prompt);
        } catch (Exception e) {
            throw new RuntimeException("Error en la solicitud a la API de ChatGPT: " + e.getMessage(), e);
        }
        return parseQuestion(response);
    }

    private String generatePrompt(String category, String difficulty) {
        return "Crea una pregunta para trivia de dificultad " + difficulty + " para la categoria " + category +
                "Inclute 4 respuestas para la pregunta y especifica la correcta en un fortmato JSON como este: " +
                "{\"question\": \"Your question?\", \"options\": [\"option1\", \"option2\", \"option3\", \"option4\"], \"answer\": \"correct option\"}";
    }

    private Question parseQuestion(String response) throws JSONException {
        try {
            JSONObject json = new JSONObject(response);
            String questionText = json.getString("question");
            String correctAnswer = json.getString("answer");
            String[] optionsArray = json.getJSONArray("options").join(",").split(",");
            return new Question(questionText, Arrays.asList(optionsArray), correctAnswer);
        } catch (JSONException e) {
            System.err.println("Error al analizar la respuesta JSON de la API de ChatGPT: " + e.getMessage());
            throw e;
        }
    }

}
