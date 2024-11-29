package ObligatorioDDA_IS.Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.Interfaces.Observer;
import ObligatorioDDA_IS.Models.Notification;
import ObligatorioDDA_IS.Models.User;
import ObligatorioDDA_IS.Repository.NotificationRepository;

/**
 * Clase RankingSystem que gestiona el ranking de los usuarios
 * y notifica a los observadores cuando suben de posición.
 */
@Service
public class RankingSystem {

    private final List<Observer> observers = new ArrayList<>();
    private final List<User> ranking = new ArrayList<>();
    private final NotificationRepository notificationRepository;

    @Autowired
    public RankingSystem(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Métodos para gestionar observadores
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String username, Long userId, int newPosition) {
        for (Observer observer : observers) {
            observer.update(username, newPosition);
        }

        // Crear y guardar la notificación
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage("¡Felicidades " + username + "! Ahora estás en la posición #" + newPosition);
        notificationRepository.save(notification);
    }

    public void updateRanking(User user) {
        System.out.println(
                "Actualizando ranking para usuario: " + user.getUsername() + ", MaxScoreSP: " + user.getMaxScoreSP());

        // Remueve al usuario de la lista si ya está presente
        ranking.removeIf(existingUser -> existingUser.getId().equals(user.getId()));

        // Agrega al usuario actualizado
        ranking.add(user);

        // Ordenar el ranking según MaxScoreSP en orden descendente
        ranking.sort(Comparator.comparingInt(User::getMaxScoreSP).reversed());

        // Mostrar el ranking actualizado en la consola
        System.out.println("Ranking después de actualizar:");
        ranking.forEach(u -> System.out.println(u.getUsername() + " - " + u.getMaxScoreSP()));

        // Notificar a los observadores de su nueva posición
        int newPosition = ranking.indexOf(user) + 1;
        notifyObservers(user.getUsername(), user.getId(), newPosition);
    }

    public List<User> getRanking() {
        return Collections.unmodifiableList(ranking);
    }
}
