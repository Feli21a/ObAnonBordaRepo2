package ObligatorioDDA_IS.Controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ObligatorioDDA_IS.Models.Question;
import ObligatorioDDA_IS.Models.SinglePlayerGame;
import ObligatorioDDA_IS.Services.QuestionService;
import ObligatorioDDA_IS.Services.SPGameService;

@RestController
@RequestMapping("/question")
public class QuestionController {

    private final SPGameService gameService;
    private final QuestionService questionService;

    @Autowired
    public QuestionController(SPGameService gameService, QuestionService questionService) {
        this.gameService = gameService;
        this.questionService = questionService;
    }

    @PostMapping("/fetch")
    public ResponseEntity<Map<String, Object>> fetchQuestion(@RequestParam Long gameId, @RequestParam String category) {
        try {
            SinglePlayerGame game = gameService.findGameById(gameId);

            if (game == null) {
                return ResponseEntity.status(404).body(Collections.singletonMap("error", "Game not found."));
            }

            if ("Completado".equals(game.getStatus())) {
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "Game has already ended."));
            }

            Question question = questionService.fetchQuestion(category, game.getDifficulty());
            if (question == null) {
                return ResponseEntity.status(500).body(
                        Collections.singletonMap("error", "No question found for the given category and difficulty."));
            }

            game.setCurrentQuestion(question);
            gameService.saveGame(game);

            // Construir la respuesta con "correctAnswer"
            Map<String, Object> response = new HashMap<>();
            response.put("question", question.getQuestionText());
            response.put("options", question.getOptions());
            response.put("correctAnswer", question.getCorrectAnswer()); // Incluir la respuesta correcta

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Collections.singletonMap("error", "Internal Server Error: " + e.getMessage()));
        }
    }

}
