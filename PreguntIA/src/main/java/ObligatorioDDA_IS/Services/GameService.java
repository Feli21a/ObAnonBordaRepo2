package ObligatorioDDA_IS.Services;

import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.Models.SinglePlayerGame;
import ObligatorioDDA_IS.Repository.GameRepository;

@Service
public class GameService {

    private GameRepository gameRepository;

    public SinglePlayerGame startNewSinglePlayerGame(String difficulty) {
        // Crear una nueva instancia de SinglePlayerGame con la dificultad seleccionada
        SinglePlayerGame game = new SinglePlayerGame(difficulty);

        // Iniciar el juego
        game.startSinglePlayerGame();

        // Guardar el juego en la base de datos
        return gameRepository.save(game);
    }
}
