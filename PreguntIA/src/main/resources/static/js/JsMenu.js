// /js/menuHandler.js
// Mostrar el nombre de usuario en el modal de perfil
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

// Manejo de Cierre de Sesión
document.getElementById("logoutButton").addEventListener("click", function () {
    fetch("/api/users/logout", {
        method: "POST"
    })
        .then(response => {
            if (response.ok) {
                window.location.href = "/login"; // Redirige a la página de inicio de sesión
            } else {
                console.error("Error al cerrar sesión");
            }
        })
        .catch(error => {
            console.error("Error de conexión al cerrar sesión", error);
        });
});

// Manejo de Cierre de Sesión
document.getElementById("logoutButton").addEventListener("click", function () {
    // Muestra el modal de confirmación
    const logoutModal = new bootstrap.Modal(document.getElementById('logoutModal'));
    logoutModal.show();
});

// Confirma el cierre de sesión
document.getElementById("confirmLogoutButton").addEventListener("click", function () {
    fetch("/api/users/logout", {
        method: "POST"
    })
        .then(response => {
            if (response.ok) {
                window.location.href = "/login"; // Redirige a la página de inicio de sesión
            } else {
                console.error("Error al cerrar sesión");
            }
        })
        .catch(error => {
            console.error("Error de conexión al cerrar sesión", error);
        });
});

// Función para iniciar el juego en una dificultad seleccionada
function startGame(dificultad) {
    console.log("Juego iniciado con dificultad:", dificultad);
    const modal = bootstrap.Modal.getInstance(document.getElementById('dificultadModal'));
    modal.hide();
}

export { startGame };
