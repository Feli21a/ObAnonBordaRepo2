package ObligatorioDDA_IS.Models;

import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SinglePlayerGame implements Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idGame;

    private String gameType = "Un Jugador";
    private LocalDateTime startDateTime;
    private int score;
    private String status;
    private Duration duration;
    private String difficulty;
    private boolean gameEnded; // Indicador para finalizar el juego si el jugador falla

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
        this.duration = Duration.between(startDateTime, LocalDateTime.now());
        this.status = "Completado";
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

    // Getters and Setters

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
        return score;
    }

    @Override
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
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

}
