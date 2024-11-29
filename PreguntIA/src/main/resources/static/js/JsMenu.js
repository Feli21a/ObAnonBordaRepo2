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
            console.log("Datos recibidos del backend:", data);
            const { username, avatar, maxScoreSP, totalCorrectQuestions } = data;

            // Configurar el perfil en la interfaz
            document.getElementById("usernameDisplay").textContent = username || "Sin Nombre";
            document.getElementById("currentAvatar").src = avatar || "/img/Avatar1.png";
            document.getElementById("maxScoreSPDisplay").textContent = maxScoreSP || "0";

            // Mostrar las preguntas correctas totales
            document.getElementById("totalCorrectQuestions").textContent = totalCorrectQuestions || "0";
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
window.startGame = async function startGame(difficulty) {
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
};

import AlertManager from './alertManager.js';

// Crear una instancia del AlertManager
const alertManager = new AlertManager("alertMessage");

// Lógica para actualizar el nombre de usuario
document.getElementById("saveUsernameButton").addEventListener("click", async function () {
    const newUsername = document.getElementById("newUsername").value.trim();

    if (!newUsername) {
        alertManager.showAlert("El nombre de usuario no puede estar vacío.", "danger");
        return;
    }

    try {
        const response = await fetch('/api/users/update-username', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username: newUsername })
        });

        if (response.ok) {
            alertManager.showAlert("Nombre de usuario actualizado con éxito.", "success");
            setTimeout(() => location.reload(), 2000); // Recargar después de 2 segundos
        } else {
            const errorMessage = await response.text();
            alertManager.showAlert(`Error al actualizar el nombre de usuario: ${errorMessage}`, "danger");
        }
    } catch (error) {
        console.error("Error al actualizar el nombre de usuario:", error);
        alertManager.showAlert("Error inesperado. Inténtalo más tarde.", "danger");
    }
});

// Lógica para actualizar la contraseña
document.getElementById("savePasswordButton").addEventListener("click", async function () {
    const newPassword = document.getElementById("newPassword").value.trim();

    if (!newPassword) {
        alertManager.showAlert("La contraseña no puede estar vacía.", "danger");
        return;
    }

    try {
        const response = await fetch('/api/users/update-password', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ password: newPassword })
        });

        if (response.ok) {
            alertManager.showAlert("Contraseña actualizada con éxito. Redirigiendo al inicio de sesión...", "success");
            setTimeout(() => {
                window.location.href = "/login"; // Redirige al inicio de sesión
            }, 2000);
        } else {
            const errorMessage = await response.text();
            alertManager.showAlert(`Error al actualizar la contraseña: ${errorMessage}`, "danger");
        }

    } catch (error) {
        console.error("Error al actualizar la contraseña:", error);
        alertManager.showAlert("Error inesperado. Inténtalo más tarde.", "danger");
    }
});


document.getElementById("confirmLogoutButton").addEventListener("click", async function () {
    try {
        // Realiza una solicitud al endpoint de logout
        const response = await fetch("api/users/logout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            // Cierre de sesión exitoso
            console.log("Sesión cerrada con éxito");
            // Redirige al usuario a la página de inicio de sesión
            window.location.href = "/login";
        } else {
            // Muestra un error si la respuesta no es satisfactoria
            console.error("Error al cerrar sesión");
        }
    } catch (error) {
        // Manejo de errores
        console.error("Error en la solicitud de logout:", error);
    }
});

document.addEventListener("DOMContentLoaded", function () {
    // Verificar si el usuario está autenticado
    fetch("/api/users/perfil")
        .then(response => {
            if (response.ok) {
                // Usuario autenticado: mostrar el botón de salir
                document.getElementById("logoutButton").classList.remove("d-none");
                document.getElementById("loginButton").classList.add("d-none");
            } else {
                // Usuario no autenticado: mostrar el botón de ingresar
                document.getElementById("loginButton").classList.remove("d-none");
                document.getElementById("logoutButton").classList.add("d-none");
            }
        })
        .catch(error => {
            console.error("Error al verificar el estado del usuario:", error);
            // En caso de error, asumimos que no hay usuario autenticado
            document.getElementById("loginButton").classList.remove("d-none");
            document.getElementById("logoutButton").classList.add("d-none");
        });
});

async function loadRanking() {
    const rankingTableBody = document.querySelector('.ranking-table tbody');

    try {
        // Realiza una solicitud al backend para obtener el ranking
        const response = await fetch('/api/ranking/list');
        if (!response.ok) throw new Error("Error al obtener los datos del ranking");

        const rankingData = await response.json();

        // Limpia la tabla antes de insertar nuevos datos
        rankingTableBody.innerHTML = '';

        // Recorre los usuarios y genera las filas dinámicamente
        rankingData.forEach((user, index) => {
            const row = `
                <tr>
                    <td>${index + 1} ${index === 0 ? '<i class="bi bi-trophy-fill text-warning"></i>' : ''}</td>
                    <td>
                        <div class="d-flex align-items-center">
                            <img src="${user.avatar || '/img/Avatar1.png'}" alt="Avatar" class="ranking-avatar me-2">
                            ${user.username}
                        </div>
                    </td>
                    <td>${user.maxScoreSP}</td>
                </tr>
            `;
            rankingTableBody.insertAdjacentHTML('beforeend', row);
        });
    } catch (error) {
        console.error("Error al cargar el ranking:", error);
        rankingTableBody.innerHTML = '<tr><td colspan="3">Error al cargar el ranking</td></tr>';
    }
}

// Llamar a la función `loadRanking` cada vez que se abra el modal de ranking
document.getElementById('rankingModal').addEventListener('show.bs.modal', loadRanking);
