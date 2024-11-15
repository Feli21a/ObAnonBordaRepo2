// /jsMenu.js

// Cargar el nombre de usuario en el modal de perfil
document.getElementById("perfilModal").addEventListener("show.bs.modal", function () {
    fetch("/api/users/perfil")
        .then(response => {
            if (response.ok) {
                return response.text(); // Obtener el nombre de usuario
            } else {
                throw new Error("No se pudo cargar el perfil");
            }
        })
        .then(username => {
            document.getElementById("usernameDisplay").textContent = username;
        })
        .catch(error => {
            console.error(error);
        });
});

// Configuración de Logout
document.getElementById("confirmLogoutButton").addEventListener("click", function () {
    fetch("/api/users/logout", {
        method: "POST"
    })
        .then(response => {
            if (response.ok) {
                window.location.href = "/login"; // Redirige a la página de inicio de sesión después del logout
            } else {
                console.error("Error al cerrar sesión");
            }
        })
        .catch(error => {
            console.error("Error de conexión al cerrar sesión", error);
        });
});


// JsMenu.js
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





