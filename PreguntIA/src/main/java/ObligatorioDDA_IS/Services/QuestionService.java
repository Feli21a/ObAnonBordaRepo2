package ObligatorioDDA_IS.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.APIs.ChatGPTAPIClient;

@Service
public class QuestionService {

    private final ChatGPTAPIClient apiClient;

    @Autowired
    public QuestionService(ChatGPTAPIClient apiClient) {
        this.apiClient = apiClient;
    }

    public String fetchQuestion(String category, String difficulty) {
        String prompt = "Generate a " + difficulty + " level question in the category of " + category +
                " with 4 answer options, and indicate the correct answer.";

        return apiClient.sendRequest(prompt);
    }
}
