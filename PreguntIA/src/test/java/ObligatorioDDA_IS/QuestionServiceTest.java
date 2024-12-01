package ObligatorioDDA_IS;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.configurationprocessor.json.JSONException;

import ObligatorioDDA_IS.APIs.ChatGPTAPIClient;
import ObligatorioDDA_IS.Models.Question;
import ObligatorioDDA_IS.Services.QuestionService;

class QuestionServiceTest {

    @Mock
    private ChatGPTAPIClient apiClient;

    @InjectMocks
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchQuestion_Success() throws JSONException {
        // Datos de prueba
        String category = "Ciencia";
        String difficulty = "Dificil";
        String mockResponse = """
                {
                    "question": "¿Cuál es la fórmula química del agua?",
                    "options": ["H2O", "CO2", "O2", "H2"],
                    "answer": "H2O"
                }
                """;

        // Mock del cliente API
        when(apiClient.sendRequest(anyString())).thenReturn(mockResponse);

        // Llamar al método
        Question question = questionService.fetchQuestion(category, difficulty);

        // Verificar resultados
        assertNotNull(question);
        assertEquals("¿Cuál es la fórmula química del agua?", question.getQuestionText());
        assertEquals("H2O", question.getCorrectAnswer());
        assertTrue(question.getOptions().containsAll(List.of("H2O", "CO2", "O2", "H2"))); // Ahora debería pasar

        // Verificar interacción con el cliente API
        verify(apiClient, atLeastOnce()).sendRequest(anyString());
    }

    @Test
    void testFetchQuestion_RetryOnDuplicate() throws JSONException {
        // Datos de prueba
        String category = "Historia";
        String difficulty = "Facil";
        String duplicateResponse = """
                {
                    "question": "¿Quién descubrió América?",
                    "options": ["Cristóbal Colón", "Américo Vespucio", "Marco Polo", "Fernando de Magallanes"],
                    "answer": "Cristóbal Colón"
                }
                """;
        String uniqueResponse = """
                {
                    "question": "¿En qué año comenzó la Primera Guerra Mundial?",
                    "options": ["1914", "1939", "1812", "1804"],
                    "answer": "1914"
                }
                """;

        // Mock del cliente API para simular reintentos
        when(apiClient.sendRequest(anyString()))
                .thenReturn(duplicateResponse)
                .thenReturn(uniqueResponse);

        // Simular pregunta previa
        questionService.fetchQuestion(category, difficulty);

        // Llamar al método con reintento
        Question question = questionService.fetchQuestion(category, difficulty);

        // Verificar que la pregunta devuelta sea única
        assertNotNull(question);
        assertEquals("¿En qué año comenzó la Primera Guerra Mundial?", question.getQuestionText());
        verify(apiClient, times(2)).sendRequest(anyString());
    }

    @Test
    void testFetchQuestion_InvalidJSONResponse() {
        // Datos de prueba
        String category = "Entretenimiento";
        String difficulty = "Normal";
        String invalidResponse = """
                {
                    "invalid": "response"
                }
                """;

        // Mock del cliente API
        when(apiClient.sendRequest(anyString())).thenReturn(invalidResponse);

        // Verificar que se lanza una excepción RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> questionService.fetchQuestion(category, difficulty));

        // Verificar que la causa de la excepción es JSONException
        assertTrue(exception.getCause() instanceof JSONException);

        // Verificar interacción con el cliente API
        verify(apiClient, times(1)).sendRequest(anyString());
    }

    @Test
    void testFetchQuestion_GeneratedPrompt() throws JSONException {
        String category = "Deporte";
        String difficulty = "Facil";

        // Mock de la API
        String mockResponse = """
                {
                    "question": "¿Qué deporte se practica con una raqueta?",
                    "options": ["Tenis", "Fútbol", "Béisbol", "Natación"],
                    "answer": "Tenis"
                }
                """;
        when(apiClient.sendRequest(anyString())).thenReturn(mockResponse);

        // Llamar a fetchQuestion
        questionService.fetchQuestion(category, difficulty);

        // Verificar que el método sendRequest recibe un prompt que contiene las
        // palabras clave esperadas
        verify(apiClient).sendRequest(argThat(prompt -> prompt.contains("categoria") &&
                prompt.contains("dificultad") &&
                prompt.contains("JSON")));
    }

}
