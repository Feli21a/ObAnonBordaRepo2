// Al abrir el modal de perfil, cargar los datos del usuario
document.addEventListener("DOMContentLoaded", function () {
    fetch("/api/users/perfil")
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al obtener el perfil del usuario");
            }
            return response.json();
        })
        .then(data => {
            const { username, avatar, maxScoreSP } = data;

            // Configurar el perfil en la interfaz
            document.getElementById("usernameDisplay").textContent = username || "Sin Nombre";
            document.getElementById("currentAvatar").src = avatar || "/img/MundiTriste.png";
            document.getElementById("maxScoreSPDisplay").textContent = maxScoreSP || "0";
        })
        .catch(error => console.error("Error al cargar el perfil del usuario:", error));
});


// Manejar la selección de un avatar desde el modal
document.querySelectorAll('.avatar-option').forEach(avatar => {
    avatar.addEventListener('click', async function () {
        const selectedAvatar = this.getAttribute('data-avatar');

        // Actualizar el avatar en el frontend inmediatamente
        document.getElementById("currentAvatar").src = selectedAvatar;

        try {
            // Guardar el avatar seleccionado en el servidor
            const response = await fetch('/api/users/update-avatar', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ avatar: selectedAvatar }), // Enviar la URL del avatar al backend
            });

            if (response.ok) {
                console.log('Avatar actualizado correctamente');
            } else {
                console.error('Error al actualizar el avatar');
            }
        } catch (error) {
            console.error("Error al guardar el avatar:", error);
        }

        // Cerrar el modal de selección de avatar
        const avatarSelectModal = bootstrap.Modal.getInstance(document.getElementById("avatarSelectModal"));
        avatarSelectModal.hide();
    });
});

// Abrir el modal de selección de avatar al hacer clic en el avatar actual
document.getElementById("perfil-avatar-container").addEventListener("click", function () {
    const avatarSelectModal = new bootstrap.Modal(document.getElementById("avatarSelectModal"));
    avatarSelectModal.show();
});

// Función para iniciar un juego con dificultad seleccionada
async function startGame(difficulty) {
    try {
        const response = await fetch(`/spgame/start?difficulty=${difficulty}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });

        if (!response.ok) throw new Error("Error al iniciar el juego");

        const data = await response.json();
        console.log("Respuesta del servidor:", data);

        if (data.gameId) {
            // Guarda el `gameId` y `difficulty` en sessionStorage
            sessionStorage.setItem('gameId', data.gameId);
            sessionStorage.setItem('difficulty', difficulty);

            // Redirige a la pantalla de la ruleta
            window.location.href = `/ruleta?gameId=${data.gameId}&difficulty=${difficulty}`;
        } else {
            console.error("Error: `gameId` no se recibió en la respuesta del servidor");
        }
    } catch (error) {
        console.error("Error en startGame:", error);
    }
}
