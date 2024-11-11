package ObligatorioDDA_IS.Models;

import java.time.Duration;
import java.time.LocalDateTime;

public interface Game {

    void startGame();

    void endGame();

    void updateScore(int points);

    int getScore();

    String getStatus();

    LocalDateTime getStartDateTime();

    Duration getDuration();

    int getIdGame();
}
