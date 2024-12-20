package ObligatorioDDA_IS.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class SinglePlayerGame implements Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGame;

    private String gameType = "Un Jugador";
    private LocalDateTime startDateTime;
    private int score;
    private String status;
    private String difficulty;
    private boolean gameEnded; // Indicador para finalizar el juego si el jugador falla

    @Transient
    private Question currentQuestion; // Pregunta actual de la partida

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Relación con el usuario
    private User user;

    public SinglePlayerGame(String difficulty) {
        this.difficulty = difficulty;
        this.status = "No ha empezado";
        this.score = 0;
        this.gameEnded = false;
    }

    public SinglePlayerGame() {

    }

    @Override
    public void startGame() {
        this.startDateTime = LocalDateTime.now();
        this.status = "En Progreso";
        System.out.println("Comenzando partida de Un Jugador en dificultad: " + difficulty);
    }

    @Override
    public void endGame() {
        this.status = "Finalizado";
        this.gameEnded = true;
    }

    @Override
    public void updateScore(int points) {
        if (!gameEnded) {
            this.score += points;
        }
    }

    // Métodos específicos para SinglePlayerGame
    public void playerFailed() {
        endGame(); // Finaliza el juego si el jugador comete un error
    }

    // Métodos para la pregunta actual
    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question question) {
        this.currentQuestion = question;
    }

    // Getters and Setters

    public Long getIdGame() {
        return idGame;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public User getUser() {
        return user;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public boolean getGameEnded() {
        return gameEnded;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public void setGameEnded(boolean gameEnd) {
        this.gameEnded = gameEnd;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
