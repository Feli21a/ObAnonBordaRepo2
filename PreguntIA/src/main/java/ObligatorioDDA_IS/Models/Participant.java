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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Relación con el usuario
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false) // Relación con la partida
    private SinglePlayerGame game;

    // Constructor, Getters y Setters
    public Participant(User user, SinglePlayerGame game) {
        this.user = user;
        this.game = game;
        this.score = 0;
    }

    public void addScore(int points) {
        this.score += points;
    }
}