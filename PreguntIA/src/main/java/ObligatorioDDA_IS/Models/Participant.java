package ObligatorioDDA_IS.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idParticipant;
    private int score;
    private int userId;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    public Participant() {
    }

    public Participant(int userId, Game game) {
        this.userId = userId;
        this.game = game;
        this.score = 0;
    }

    public void addScore(int points) {
        this.score += points;
    }

    // Getters and setters
}
