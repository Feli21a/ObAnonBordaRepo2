package ObligatorioDDA_IS.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ObligatorioDDA_IS.Models.SinglePlayerGame;
import ObligatorioDDA_IS.Services.GameService;

@RestController
@RequestMapping("/start-game")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> startSinglePlayerGame(@RequestParam String difficulty) {
        // Iniciar el juego de un solo jugador
        SinglePlayerGame game = gameService.startNewSinglePlayerGame(difficulty);

        // Preparar la respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("gameId", game.getIdGame());
        response.put("status", "Game started");

        return ResponseEntity.ok(response);

    }
}
