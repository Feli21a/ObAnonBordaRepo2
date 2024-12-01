package ObligatorioDDA_IS;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ObligatorioDDA_IS.Models.User;
import ObligatorioDDA_IS.Services.RankingSystem;

class RankingSystemTest {

    private RankingSystem rankingSystem;

    @BeforeEach
    void setUp() {
        rankingSystem = new RankingSystem();
    }

    @Test
    void testUpdateRanking_AddNewUser() {
        // Datos de prueba
        User user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setMaxScoreSP(100);

        // Llamar al método
        rankingSystem.updateRanking(user);

        // Verificar
        List<User> ranking = rankingSystem.getRanking();
        assertEquals(1, ranking.size());
        assertEquals(user.getUsername(), ranking.get(0).getUsername());
        assertEquals(user.getMaxScoreSP(), ranking.get(0).getMaxScoreSP());
    }

    @Test
    void testUpdateRanking_UpdateExistingUser() {
        // Usuario inicial
        User user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setMaxScoreSP(100);

        // Agregar usuario al ranking
        rankingSystem.updateRanking(user);

        // Actualizar datos del usuario
        user.setMaxScoreSP(200);
        rankingSystem.updateRanking(user);

        // Verificar
        List<User> ranking = rankingSystem.getRanking();
        assertEquals(1, ranking.size());
        assertEquals(200, ranking.get(0).getMaxScoreSP());
    }

    @Test
    void testUpdateRanking_MultipleUsers() {
        // Datos de prueba
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("User1");
        user1.setMaxScoreSP(150);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("User2");
        user2.setMaxScoreSP(200);

        User user3 = new User();
        user3.setId(3L);
        user3.setUsername("User3");
        user3.setMaxScoreSP(100);

        // Agregar usuarios
        rankingSystem.updateRanking(user1);
        rankingSystem.updateRanking(user2);
        rankingSystem.updateRanking(user3);

        // Verificar orden
        List<User> ranking = rankingSystem.getRanking();
        assertEquals(3, ranking.size());
        assertEquals("User2", ranking.get(0).getUsername());
        assertEquals("User1", ranking.get(1).getUsername());
        assertEquals("User3", ranking.get(2).getUsername());
    }

    @Test
    void testGetRanking_ImmutableList() {
        // Datos de prueba
        User user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setMaxScoreSP(100);

        // Agregar usuario
        rankingSystem.updateRanking(user);

        // Intentar modificar el ranking
        List<User> ranking = rankingSystem.getRanking();
        assertThrows(UnsupportedOperationException.class, () -> ranking.add(new User()));
    }
}
