package ObligatorioDDA_IS.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.Models.Notification;
import ObligatorioDDA_IS.Repository.NotificationRepository;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getUserNotifications(Long userId) {
        // Devuelve todas las notificaciones de un usuario
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        // Devuelve las notificaciones no leídas de un usuario
        return notificationRepository.findByUserIdAndReadFalse(userId);
    }

    public Notification createNotification(Long userId, String message) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        return notificationRepository.save(notification);
    }

    public void markNotificationAsRead(Long notificationId) {
        // Marca una notificación como leída
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("La notificación no existe");
        }
        notificationRepository.deleteById(id);
    }
}
