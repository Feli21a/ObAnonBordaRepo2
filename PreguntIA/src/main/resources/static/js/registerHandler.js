document.addEventListener("DOMContentLoaded", () => {
    const alertMessage = document.getElementById("alertMessage");

    document.getElementById("registerForm").addEventListener("submit", function (event) {
        event.preventDefault(); // Evita el envío normal del formulario

        const formData = new FormData(this); // Captura los datos del formulario
        const urlParams = new URLSearchParams(formData).toString(); // Convierte a formato URL-encoded

        fetch("/api/users/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: urlParams
        })
            .then(response => {
                if (response.ok) {
                    return response.text(); // Obtiene el mensaje en caso de éxito
                } else {
                    throw response; // Lanza el error para manejarlo en el catch
                }
            })
            .then(message => {
                // Muestra el mensaje de éxito y redirige después de 2 segundos
                alertMessage.className = "alert alert-success";
                alertMessage.textContent = message;
                alertMessage.classList.remove("d-none");

                setTimeout(() => {
                    window.location.href = "/login"; // Redirige a la página de login
                }, 2000);
            })
            .catch(error => {
                // Muestra el mensaje de error
                error.text().then(errorMessage => {
                    alertMessage.className = "alert alert-danger";
                    alertMessage.textContent = errorMessage || "Error en el registro";
                    alertMessage.classList.remove("d-none");

                    // Opcional: Oculta el mensaje de error después de 3 segundos
                    setTimeout(() => {
                        alertMessage.classList.add("d-none");
                    }, 3000);
                });
            });
    });
});

function togglePasswordVisibility(inputId) {
    const input = document.getElementById(inputId);
    const button = input.nextElementSibling;
    const icon = button.querySelector('i');

    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('bi-eye-fill');
        icon.classList.add('bi-eye-slash-fill');
    } else {
        input.type = 'password';
        icon.classList.remove('bi-eye-slash-fill');
        icon.classList.add('bi-eye-fill');
    }
}
