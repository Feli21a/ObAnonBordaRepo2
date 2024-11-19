document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("loginForm").addEventListener("submit", function (event) {
        event.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        fetch("/api/users/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        })
            .then(response => {
                if (response.ok) {
                    return response.text(); // Espera la redirección
                } else {
                    throw response;
                }
            })
            .then(location => {
                window.location.href = location; // Redirige al menú
            })
            .catch(error => {
                if (error.text) {
                    error.text().then(errorMessage => {
                        mostrarMensajeError(errorMessage || "Error en el inicio de sesión");
                    });
                } else {
                    mostrarMensajeError("Error inesperado");
                }
            });
    });
});

function togglePasswordVisibility(inputId) {
    const input = document.getElementById(inputId);
    const button = input.parentElement.querySelector(".toggle-password");
    const icon = button.querySelector("i");

    if (input.type === "password") {
        input.type = "text";
        icon.classList.remove("bi-eye-fill");
        icon.classList.add("bi-eye-slash-fill");
    } else {
        input.type = "password";
        icon.classList.remove("bi-eye-slash-fill");
        icon.classList.add("bi-eye-fill");
    }
}