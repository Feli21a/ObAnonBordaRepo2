package ObligatorioDDA_IS.Models;

import org.springframework.boot.configurationprocessor.json.JSONException;

import ObligatorioDDA_IS.Services.QuestionService;
import jakarta.persistence.Entity;

@Entity
public class SinglePlayerGame extends Game {

    private String difficulty; // Nivel de dificultad seleccionado

    public SinglePlayerGame(String difficulty) {
        super("Single Player", null); // Llama al constructor de Game y configura el tipo
        this.difficulty = difficulty;
    }

    // Método para iniciar el juego con la dificultad específica
    public void startSinglePlayerGame() {
        super.startGame();
        // Lógica adicional para un jugador, si es necesaria
        System.out.println("Starting single-player game with difficulty: " + difficulty);
    }

    // Método para obtener preguntas con la dificultad establecida
    public String fetchQuestion(String category, QuestionService questionService) throws JSONException {
        return questionService.fetchQuestion(category, this.difficulty);
    }

    // Getters y setters para dificultad
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
