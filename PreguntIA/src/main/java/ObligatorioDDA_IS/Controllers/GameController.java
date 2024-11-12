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
@RequestMapping("/game")
public class GameController {

    private final SPGameService gameService;
    private final QuestionService questionService;

    @Autowired
    public GameController(SPGameService gameService, QuestionService questionService) {
        this.gameService = gameService;
        this.questionService = questionService;
    }

    @PostMapping("/start-singleplayer")
    public ResponseEntity<Map<String, Object>> startSinglePlayerGame(@RequestParam String difficulty) {
        try {
            // Crea una nueva partida de un solo jugador con la dificultad especificada
            SinglePlayerGame game = gameService.createSinglePlayerGame(difficulty);

            // Inicia la partida y actualiza el estado
            gameService.startSinglePlayerGame(game.getIdGame());

            Map<String, Object> response = new HashMap<>();
            response.put("gameId", game.getIdGame());
            response.put("status", "Game started");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Internal Server Error"));
        }
    }

    @PostMapping("/fetch-question")
    public ResponseEntity<Map<String, Object>> fetchQuestion(@RequestParam int gameId, @RequestParam String category) {
        try {
            SinglePlayerGame game = gameService.findGameById(gameId);

            // Verifica si el juego ya ha terminado
            if (game.getStatus().equals("Completado")) {
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "Game has already ended."));
            }

            // Obtiene una pregunta basada en la categoría y dificultad del juego
            Question question = questionService.fetchQuestion(category, game.getDifficulty());
            game.setCurrentQuestion(question); // Guarda la pregunta actual en el juego
            gameService.saveGame(game);

            Map<String, Object> response = new HashMap<>();
            response.put("question", question);
            response.put("status", "Question fetched");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Internal Server Error"));
        }
    }

    @PostMapping("/submit-answer")
    public ResponseEntity<Map<String, Object>> submitAnswer(@RequestParam int gameId, @RequestParam String answer) {
        try {
            SinglePlayerGame game = gameService.findGameById(gameId);

            // Verifica si el juego ya ha terminado
            if (game.getStatus().equals("Completado")) {
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "Game has already ended."));
            }

            // Verifica si la respuesta es correcta
            Question currentQuestion = game.getCurrentQuestion();
            boolean isCorrect = currentQuestion.getCorrectAnswer().equals(answer);

            if (isCorrect) {
                game.updateScore(10); // Incrementa la puntuación
            } else {
                game.playerFailed(); // Finaliza el juego si falla
            }

            gameService.saveGame(game); // Guarda el estado actualizado del juego

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
