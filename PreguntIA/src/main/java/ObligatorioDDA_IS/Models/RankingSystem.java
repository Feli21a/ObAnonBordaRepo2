package ObligatorioDDA_IS.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ObligatorioDDA_IS.Interfaces.Observer;

/**
 * Clase RankingSystem que gestiona el ranking de los usuarios
 * y notifica a los observadores cuando suben de posición.
 */
public class RankingSystem {

    private final List<Observer> observers = new ArrayList<>();
    private final List<User> ranking = new ArrayList<>();
    
    // Métodos para gestionar observadores
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    // Método para notificar cambios
    private void notifyObservers(String username, int newPosition) {
        for (Observer observer : observers) {
            observer.update(username, newPosition);
        }
    }

    // Actualizar el ranking basado en MaxScoreSP
    public void updateRanking(User user) {
        if (!ranking.contains(user)) {
            ranking.add(user);
        }

        // Ordenar el ranking según MaxScoreSP en orden descendente
        ranking.sort(Comparator.comparingInt(User::getMaxScoreSP).reversed());

        // Notificar a los observadores de su nueva posición
        int newPosition = ranking.indexOf(user) + 1;
        notifyObservers(user.getUsername(), newPosition);
    }

    public List<User> getRanking() {
        System.out.println("Ranking size: " + ranking.size());
        return Collections.unmodifiableList(ranking);
    }

}
