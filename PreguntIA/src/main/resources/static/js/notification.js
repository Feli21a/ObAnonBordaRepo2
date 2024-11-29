async function loadNotifications() {
    const userId = sessionStorage.getItem("userId");
    if (!userId) {
        console.error("No se encontró un userId válido.");
        return;
    }

    try {
        const response = await fetch(`/api/notifications/user?userId=${userId}`);
        if (!response.ok) throw new Error("Error al cargar las notificaciones");

        const notifications = await response.json();
        const notificationsList = document.querySelector(".inbox-list");

        notificationsList.innerHTML = ""; // Limpia la lista existente

        notifications.forEach(notification => {
            const notificationItem = `
                <li class="inbox-item" data-id="${notification.id}">
                    ${notification.message}
                    <button class="btn btn-sm btn-outline-danger" onclick="markAsRead(${notification.id})">
                        <i class="bi bi-trash3"></i>
                    </button>
                </li>`;
            notificationsList.insertAdjacentHTML("beforeend", notificationItem);
        });

    } catch (error) {
        console.error("Error al cargar las notificaciones:", error);
    }
}


async function markAsRead(notificationId) {
    try {
        const response = await fetch(`/api/notifications/${notificationId}`, {
            method: "DELETE",
        });

        if (!response.ok) throw new Error("Error al eliminar la notificación");

        // Elimina la notificación del DOM
        const notificationElement = document.querySelector(`.inbox-item[data-id="${notificationId}"]`);
        if (notificationElement) {
            notificationElement.remove();
        }

        console.log("Notificación eliminada correctamente");
    } catch (error) {
        console.error("Error al eliminar la notificación:", error);
    }
}



// Cargar notificaciones al abrir el modal de bandeja
document.getElementById('inboxModal').addEventListener('show.bs.modal', loadNotifications);
