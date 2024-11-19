package ObligatorioDDA_IS.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.Models.Question;
import ObligatorioDDA_IS.Models.SinglePlayerGame;
import ObligatorioDDA_IS.Models.User;
import ObligatorioDDA_IS.Repository.SPGameRepository;
import ObligatorioDDA_IS.Repository.UserRepository;

@Service
public class SPGameService {

    private final SPGameRepository gameRepository;

    @Autowired
    public SPGameService(SPGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Autowired
    private UserRepository userRepository;

    public SinglePlayerGame createSinglePlayerGame(String difficulty, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        SinglePlayerGame game = new SinglePlayerGame(difficulty);
        game.setUser(user); // Vincula el usuario al juego
        gameRepository.save(game);

        return game;
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
                .orElseThrow(() -> new RuntimeException("Game not found"));

        game.endGame();
        gameRepository.save(game);

        // Actualizar maxScoreSP si corresponde
        User user = game.getUser();
        if (game.getScore() > user.getMaxScoreSP()) {
            user.setMaxScoreSP(game.getScore());
            userRepository.save(user);
        }
    }

    public Question getCurrentQuestion(int gameId) {
        SinglePlayerGame game = findGameById(gameId);
        return game.getCurrentQuestion();
    }

    public void updateScore(int gameId, int score) {
        SinglePlayerGame game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (!game.getGameEnded()) {
            game.setScore(score); // Actualiza el puntaje
            gameRepository.save(game);
        } else {
            throw new RuntimeException("No se puede actualizar el puntaje de un juego finalizado.");
        }
    }
}
