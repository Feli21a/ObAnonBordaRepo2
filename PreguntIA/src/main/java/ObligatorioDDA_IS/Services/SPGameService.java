package ObligatorioDDA_IS.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.Models.Question;
import ObligatorioDDA_IS.Models.SinglePlayerGame;
import ObligatorioDDA_IS.Repository.SPGameRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class SPGameService {

    private final SPGameRepository gameRepository;

    @Autowired
    public SPGameService(SPGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public SinglePlayerGame createSinglePlayerGame(String difficulty) {
        SinglePlayerGame game = new SinglePlayerGame(difficulty);
        return gameRepository.save(game);
    }

    public SinglePlayerGame findGameById(int gameId) {
        Optional<SinglePlayerGame> game = gameRepository.findById(gameId);
        return game.orElseThrow(() -> new IllegalArgumentException("Game not found"));
    }

    public void startSinglePlayerGame(int gameId) {
        SinglePlayerGame game = findGameById(gameId);
        game.startGame();
        gameRepository.save(game);
    }

    public void saveGame(SinglePlayerGame game) {
        gameRepository.save(game);
    }

    public void endGame(int gameId) {
        SinglePlayerGame game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Partida no encontrada"));

        game.setStatus("Finalizada"); // Cambia el estado a "Finalizada"
        game.setGameEnded(true); // Marca que el juego ha terminado
        gameRepository.save(game); // Guarda los cambios en la base de datos
    }

    public Question getCurrentQuestion(int gameId) {
        SinglePlayerGame game = findGameById(gameId);
        return game.getCurrentQuestion();
    }
}
