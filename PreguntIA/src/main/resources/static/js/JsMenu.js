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

function startGame(difficulty) {
    // Define el endpoint de tu API para iniciar el juego
    const url = `/start-game?difficulty=${difficulty}`;

    // Realiza la petición al backend
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log("Game started with difficulty:", difficulty);
            // Redirigir a la pantalla del juego (ajusta la URL según sea necesario)
            window.location.href = `/game/${data.gameId}`;
        })

        .catch(error => {
            console.error("Error starting game:", error);
            alert("Hubo un problema al iniciar el juego. Por favor, inténtalo de nuevo.");
        });

}

export { startGame };
