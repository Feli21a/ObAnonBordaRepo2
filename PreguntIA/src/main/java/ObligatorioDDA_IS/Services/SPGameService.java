package ObligatorioDDA_IS.Services;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.Models.Question;
import ObligatorioDDA_IS.Models.SinglePlayerGame;
import ObligatorioDDA_IS.Models.User;
import ObligatorioDDA_IS.Repository.SPGameRepository;
import ObligatorioDDA_IS.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

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

    @Transactional
    public void endGame(Long gameId, Map<String, Integer> scoreData, HttpSession session) {
        // Validar usuario autenticado
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        // Validar que el mapa contiene el puntaje final
        if (!scoreData.containsKey("finalScore")) {
            throw new IllegalArgumentException("Faltan datos en la solicitud: finalScore es requerido.");
        }

        int finalScore = scoreData.get("finalScore");

        // Recuperar al usuario desde la base de datos
        User userFromDb = userRepository.findById(loggedInUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar el puntaje total y el máximo
        System.out.println("Puntaje antes de la actualización: " + userFromDb.getTotalScore());
        userFromDb.setTotalScore(userFromDb.getTotalScore() + finalScore);
        if (finalScore > userFromDb.getMaxScoreSP()) {
            userFromDb.setMaxScoreSP(finalScore);
        }

        // Guardar los cambios en la base de datos
        userRepository.save(userFromDb);

        // Actualizar la sesión
        session.setAttribute("user", userFromDb);

        System.out.println("Puntaje después de la actualización: " + userFromDb.getTotalScore());
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
