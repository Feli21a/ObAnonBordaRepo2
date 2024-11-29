package ObligatorioDDA_IS.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ObligatorioDDA_IS.Models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Devuelve todas las notificaciones de un usuario
    List<Notification> findByUserId(Long userId);

    // Devuelve notificaciones no leídas de un usuario
    List<Notification> findByUserIdAndReadFalse(Long userId);
}
