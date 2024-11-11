package ObligatorioDDA_IS.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.Models.SinglePlayerGame;
import ObligatorioDDA_IS.Repository.SPGameRepository;

@Service
public class SPGameService {

    private final SPGameRepository gameRepository;

    @Autowired
    public SPGameService(SPGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // Método para crear una nueva partida de un solo jugador
    public SinglePlayerGame createSinglePlayerGame(String difficulty) {
        // Crear nueva partida para un solo jugador
        SinglePlayerGame game = new SinglePlayerGame();
        game.setDifficulty(difficulty);
        game.setGameType("SinglePlayer");
        game.setStatus("Not Started");

        // Guardar la partida
        return gameRepository.save(game);
    }

    // Método para iniciar una partida de un solo jugador
    public SinglePlayerGame startSinglePlayerGame(int gameId) {
        SinglePlayerGame game = (SinglePlayerGame) gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + gameId));

        game.startGame(); // Cambia el estado a "In Progress" y registra la fecha de inicio
        return gameRepository.save(game);
    }

    // Método para finalizar una partida de un solo jugador
    public SinglePlayerGame endSinglePlayerGame(int gameId, int finalScore) {
        SinglePlayerGame game = (SinglePlayerGame) gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + gameId));

        game.updateScore(finalScore); // Actualiza la puntuación final
        game.endGame(); // Registra la duración y cambia el estado a "Completed"
        return gameRepository.save(game);
    }
}
