package ObligatorioDDA_IS.Controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ObligatorioDDA_IS.Models.SinglePlayerGame;
import ObligatorioDDA_IS.Services.SPGameService;

@RestController
@RequestMapping("/spgame") // Ajuste de la ruta base
public class SPGameController { // Nombre actualizado de la clase

    private final SPGameService gameService;

    @Autowired
    public SPGameController(SPGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startSinglePlayerGame(@RequestParam String difficulty) {
        try {
            SinglePlayerGame game = gameService.createSinglePlayerGame(difficulty);
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

    @PostMapping("/{gameId}/end")
    public ResponseEntity<Void> finalizarJuego(@PathVariable int gameId) {
        try {
            gameService.finalizarPartida(gameId); // Llama al método para finalizar la partida en el servicio
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Devuelve un error 500 si algo falla
        }
    }
}
