// static/js/alertManager.js
export default class AlertManager {
    constructor(alertElementId) {
        this.alertElement = document.getElementById(alertElementId);
    }

    showAlert(message, type = "danger") {
        this.alertElement.className = `alert alert-${type}`;
        this.alertElement.textContent = message;
        this.alertElement.classList.remove("d-none");

        // Ocultar el mensaje después de 5 segundos
        setTimeout(() => {
            this.alertElement.classList.add("d-none");
        }, 5000);
    }
}
