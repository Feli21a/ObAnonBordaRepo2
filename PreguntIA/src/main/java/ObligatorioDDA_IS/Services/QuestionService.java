package ObligatorioDDA_IS.Services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.APIs.ChatGPTAPIClient;
import ObligatorioDDA_IS.Models.Question;

@Service
public class QuestionService {

    private final ChatGPTAPIClient apiClient;
    private final Set<String> previousQuestions = new HashSet<>(); // Almacena solo el texto de las preguntas

    @Autowired
    public QuestionService(ChatGPTAPIClient apiClient) {
        this.apiClient = apiClient;
    }

    public Question fetchQuestion(String category, String difficulty) throws JSONException {
        String prompt = generatePrompt(category, difficulty);

        // Imprime el prompt en la consola para pruebas
        System.out.println("Prompt enviado a la API: " + prompt);

        String response;
        Question question;
        try {
            do {
                response = apiClient.sendRequest(prompt);
                question = parseQuestion(response); // Parsea la respuesta para extraer el texto de la pregunta
            } while (previousQuestions.contains(question.getQuestionText())); // Reintenta si la pregunta ya existe

            previousQuestions.add(question.getQuestionText()); // Agrega solo el texto al set
        } catch (Exception e) {
            throw new RuntimeException("Error en la solicitud a la API de ChatGPT: " + e.getMessage(), e);
        }
        return question;
    }

    private String generatePrompt(String category, String difficulty) {
        String prompt = String.format(
            "Crea una pregunta en español para trivia, de dificultad %s para la categoria %s. Incluye 4 respuestas para la pregunta y especifica la correcta en un formato JSON como este: {\"question\": \"Your question?\", \"options\": [\"option1\", \"option2\", \"option3\", \"option4\"], \"answer\": \"correct option\"}. ID: %.5f",
            difficulty, category, Math.random()
        );

        // Imprimir el prompt generado en la consola
        System.out.println("Prompt generado: " + prompt);

        return prompt;
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
