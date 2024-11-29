package ObligatorioDDA_IS.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class MultiPlayerGame implements Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idGame;

    private String gameType = "Multijugador";
    private LocalDateTime startDateTime;
    private String status;
    private String difficulty;

    @Transient
    private Question currentQuestion; // Pregunta actual de la partida

    @ManyToMany // Relación con múltiples usuarios
    private List<User> players = new ArrayList<>();

    @OneToMany(mappedBy = "game") // Relación con los puntajes individuales por jugador
    private List<PlayerScore> playerScores = new ArrayList<>();

    public MultiPlayerGame(String difficulty) {
        this.difficulty = difficulty;
        this.status = "No ha empezado";
    }

    public MultiPlayerGame() {

    }

    @Override
    public void startGame() {
        this.startDateTime = LocalDateTime.now();
        this.status = "En Progreso";
        System.out.println("Comenzando partida multijugador en dificultad: " + difficulty);
    }

    @Override
    public void endGame() {
        this.status = "Finalizado";
        System.out.println("Partida multijugador finalizada.");
    }

    @Override
    public void updateScore(int points) {
        throw new UnsupportedOperationException("En el modo multijugador, los puntajes se manejan individualmente.");
    }

    // Métodos específicos para MultiPlayerGame
    public void addPlayer(User user) {
        if (!players.contains(user)) {
            players.add(user);
            playerScores.add(new PlayerScore(this, user, 0)); // Agrega un nuevo puntaje inicial para el jugador
        }
    }

    public void updatePlayerScore(User user, int points) {
        for (PlayerScore score : playerScores) {
            if (score.getUser().equals(user)) {
                score.updateScore(points);
                break;
            }
        }
    }

    // Métodos para la pregunta actual
    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question question) {
        this.currentQuestion = question;
    }

    // Getters y Setters

    @Override
    public int getIdGame() {
        return idGame;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public int getScore() {
        throw new UnsupportedOperationException("Los puntajes globales no son aplicables al modo multijugador.");
    }

    @Override
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public List<User> getPlayers() {
        return players;
    }

    public List<PlayerScore> getPlayerScores() {
        return playerScores;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
