package ObligatorioDDA_IS.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.Models.Question;
import ObligatorioDDA_IS.Models.SinglePlayerGame;
import ObligatorioDDA_IS.Repository.SPGameRepository;

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

    public Question getCurrentQuestion(int gameId) {
        SinglePlayerGame game = findGameById(gameId);
        return game.getCurrentQuestion();
    }
}
