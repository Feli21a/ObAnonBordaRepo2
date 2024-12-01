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
    private RankingSystem rankingSystem;

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

    public SinglePlayerGame findGameById(Long gameId) {
        Optional<SinglePlayerGame> game = gameRepository.findById(gameId);
        return game.orElseThrow(() -> new IllegalArgumentException("Game not found"));
    }

    public void startSinglePlayerGame(Long gameId) {
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

        // Recuperar la partida desde la base de datos
        SinglePlayerGame game = (SinglePlayerGame) gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        // Finalizar el juego: actualizar estado y puntaje
        game.endGame(); // Esto establece `gameEnded` en true y `status` en "Finalizado"
        game.setScore(finalScore); // Establece el puntaje final del juego

        // Guardar los cambios en la base de datos para el juego
        gameRepository.save(game);

        // Actualizar el puntaje total y el máximo del usuario
        System.out.println("Puntaje antes de la actualización: " + userFromDb.getTotalScore());
        userFromDb.setTotalScore(userFromDb.getTotalScore() + finalScore);
        if (finalScore > userFromDb.getMaxScoreSP()) {
            userFromDb.setMaxScoreSP(finalScore);
        }

        // Guardar los cambios en la base de datos para el usuario
        userRepository.save(userFromDb);

        // Actualizar el ranking
        rankingSystem.updateRanking(userFromDb);

        // Actualizar la sesión con los datos del usuario actualizados
        session.setAttribute("user", userFromDb);

        System.out.println("Puntaje después de la actualización: " + userFromDb.getTotalScore());
    }

    public Question getCurrentQuestion(Long gameId) {
        SinglePlayerGame game = findGameById(gameId);
        return game.getCurrentQuestion();
    }

    public void updateScore(Long gameId, int score) {
        SinglePlayerGame game = (SinglePlayerGame) gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (!game.getGameEnded()) {
            game.setScore(score); // Actualiza el puntaje
            gameRepository.save(game);
        } else {
            throw new RuntimeException("No se puede actualizar el puntaje de un juego finalizado.");
        }
    }
}
