package ObligatorioDDA_IS;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import ObligatorioDDA_IS.Models.Question;
import ObligatorioDDA_IS.Models.SinglePlayerGame;
import ObligatorioDDA_IS.Models.User;
import ObligatorioDDA_IS.Repository.SPGameRepository;
import ObligatorioDDA_IS.Repository.UserRepository;
import ObligatorioDDA_IS.Services.RankingSystem;
import ObligatorioDDA_IS.Services.SPGameService;
import jakarta.servlet.http.HttpSession;

class SPGameServiceTest {

    @Mock
    private SPGameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RankingSystem rankingSystem;

    @Mock
    private HttpSession session;

    @InjectMocks
    private SPGameService spGameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSinglePlayerGame_Success() {
        Long userId = 1L;
        String difficulty = "Dificil";

        User mockUser = new User();
        mockUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        SinglePlayerGame mockGame = new SinglePlayerGame(difficulty);
        when(gameRepository.save(any(SinglePlayerGame.class))).thenReturn(mockGame);

        SinglePlayerGame result = spGameService.createSinglePlayerGame(difficulty, userId);

        assertNotNull(result);
        assertEquals(difficulty, result.getDifficulty());
        verify(userRepository, times(1)).findById(userId);
        verify(gameRepository, times(1)).save(any(SinglePlayerGame.class));
    }

    @Test
    void testStartSinglePlayerGame_Success() {
        Long gameId = (long) 1;

        SinglePlayerGame mockGame = new SinglePlayerGame("Medio");
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));

        spGameService.startSinglePlayerGame(gameId);

        assertEquals("En Progreso", mockGame.getStatus());
        verify(gameRepository, times(1)).save(mockGame);
    }

    @Test
    void testEndGame_Success() {
        Long gameId = 1L;
        int finalScore = 100;
        Map<String, Integer> scoreData = Map.of("finalScore", finalScore);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setTotalScore(500);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        spGameService.endGame(gameId, scoreData, session);

        assertEquals(600, mockUser.getTotalScore());
        assertEquals(100, mockUser.getMaxScoreSP());
        verify(rankingSystem, times(1)).updateRanking(mockUser);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void testGetCurrentQuestion_Success() {
        Long gameId = (long) 1;

        SinglePlayerGame mockGame = new SinglePlayerGame("Facil");
        Question mockQuestion = new Question();
        mockGame.setCurrentQuestion(mockQuestion);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));

        Question result = spGameService.getCurrentQuestion(gameId);

        assertNotNull(result);
        assertEquals(mockQuestion, result);
        verify(gameRepository, times(1)).findById(gameId);
    }

    @Test
    void testUpdateScore_Success() {
        Long gameId = (long) 1;
        int score = 200;

        SinglePlayerGame mockGame = new SinglePlayerGame("Dificil");
        mockGame.setGameEnded(false);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));

        spGameService.updateScore(gameId, score);

        assertEquals(score, mockGame.getScore());
        verify(gameRepository, times(1)).save(mockGame);
    }
}
