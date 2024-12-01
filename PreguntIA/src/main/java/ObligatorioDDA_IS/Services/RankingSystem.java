package ObligatorioDDA_IS.Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.Models.User;

/**
 * Clase RankingSystem que gestiona el ranking de los usuarios.
 */
@Service
public class RankingSystem {

    private final List<User> ranking = new ArrayList<>();

    public void updateRanking(User user) {
        // Eliminar el usuario si ya está en el ranking
        ranking.removeIf(existingUser -> existingUser.getId().equals(user.getId()));

        // Agregar el usuario actualizado
        ranking.add(user);

        // Ordenar el ranking por `maxScoreSP` en orden descendente
        ranking.sort(Comparator.comparingInt(User::getMaxScoreSP).reversed());

        System.out.println("Ranking actualizado:");
        ranking.forEach(u -> System.out.println(u.getUsername() + " - " + u.getMaxScoreSP()));
    }

    public List<User> getRanking() {
        return Collections.unmodifiableList(ranking);
    }
}
