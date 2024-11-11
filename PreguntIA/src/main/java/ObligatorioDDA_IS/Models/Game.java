package ObligatorioDDA_IS.Models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idGame;

    private String gameType;
    private LocalDateTime startDateTime;
    private int score;
    private int winnerId;
    private String status;
    private Duration duration;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participant> participants;

    public Game() {
    }

    public Game(String gameType, List<Participant> participants) {
        this.gameType = gameType;
        this.participants = participants;
        this.status = "Not Started";
        this.score = 0;
    }

    public void startGame() {
        this.startDateTime = LocalDateTime.now();
        this.status = "In Progress";
    }

    public void endGame() {
        this.duration = Duration.between(startDateTime, LocalDateTime.now());
        this.status = "Completed";
    }

    public void updateScore(int points) {
        this.score += points;
    }

    public int getIdGame() {
        return idGame;
    }

    public String getGameType() {
        return gameType;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public int getScore() {
        return score;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public String getStatus() {
        return status;
    }

    public Duration getDuration() {
        return duration;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    
}
