document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("loginForm").addEventListener("submit", function (event) {
        event.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        fetch("/api/users/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams({ email, password })
        })
            .then(response => {
                if (!response.ok) {
                    throw response;
                }
                return response.json(); // Parsear la respuesta JSON
            })
            .then(data => {
                // Guardar el userId en sessionStorage
                sessionStorage.setItem("userId", data.userId);

                // Redirigir al menú
                window.location.href = data.location;
            })
            .catch(error => {
                error.json().then(errorData => {
                    const alertMessage = document.getElementById("alertMessage");
                    alertMessage.className = "alert alert-danger";
                    alertMessage.textContent =
                        errorData.error || "Error en el inicio de sesión";
                    alertMessage.classList.remove("d-none");

                    // Ocultar el mensaje después de 3 segundos
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
