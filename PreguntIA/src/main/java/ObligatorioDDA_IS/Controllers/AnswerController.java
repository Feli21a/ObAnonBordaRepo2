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
import ObligatorioDDA_IS.Services.SPGameService;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final SPGameService gameService;

    @Autowired
    public AnswerController(SPGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitAnswer(@RequestParam int gameId, @RequestParam String answer) {
        try {
            SinglePlayerGame game = gameService.findGameById(gameId);

            if (game.getStatus().equals("Completado")) {
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "Game has already ended."));
            }

            Question currentQuestion = game.getCurrentQuestion();
            boolean isCorrect = currentQuestion.getCorrectAnswer().equals(answer);

            if (isCorrect) {
                game.updateScore(10);
            } else {
                game.playerFailed();
            }

            gameService.saveGame(game);

            Map<String, Object> response = new HashMap<>();
            response.put("isCorrect", isCorrect);
            response.put("score", game.getScore());
            response.put("status", game.getStatus());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Internal Server Error"));
        }
    }
}
