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
                if (response.ok) {
                    return response.text();
                } else {
                    throw response;
                }
            })
            .then(location => {
                window.location.href = location; // Redirige a Menu.html
            })
            .catch(error => {
                error.text().then(errorMessage => {
                    const alertMessage = document.getElementById("alertMessage");
                    alertMessage.className = "alert alert-danger";
                    alertMessage.textContent = errorMessage || "Error en el inicio de sesión";
                    alertMessage.classList.remove("d-none");

                    // Oculta el mensaje de error después de 3 segundos
                    setTimeout(() => {
                        alertMessage.classList.add("d-none");
                    }, 3000);
                });
            });
    });
});