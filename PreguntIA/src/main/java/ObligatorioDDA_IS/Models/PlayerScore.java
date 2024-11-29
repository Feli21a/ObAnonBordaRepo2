package ObligatorioDDA_IS.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PlayerScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private MultiPlayerGame game;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int score;

    public PlayerScore(MultiPlayerGame game, User user, int score) {
        this.game = game;
        this.user = user;
        this.score = score;
    }

    public PlayerScore() {
    }

    public void updateScore(int points) {
        this.score += points;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public MultiPlayerGame getGame() {
        return game;
    }

    public User getUser() {
        return user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
