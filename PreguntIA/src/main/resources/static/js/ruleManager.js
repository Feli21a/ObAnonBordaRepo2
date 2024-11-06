const canvas = document.getElementById("roulette-wheel");
const ctx = canvas.getContext("2d");
const spinButton = document.getElementById("spin-button");

const sections = ["Categoría 1", "Categoría 2", "Categoría 3", "Categoría 4", "Categoría 5"];
const colors = ["#f94144", "#f3722c", "#f8961e", "#f9c74f", "#90be6d"];
let currentAngle = 0;
let spinAngle = 0;

function drawWheel() {
    const arcSize = (2 * Math.PI) / sections.length;
    for (let i = 0; i < sections.length; i++) {
        ctx.beginPath();
        ctx.fillStyle = colors[i];
        ctx.moveTo(canvas.width / 2, canvas.height / 2);
        ctx.arc(
            canvas.width / 2,
            canvas.height / 2,
            canvas.width / 2,
            currentAngle,
            currentAngle + arcSize
        );
        ctx.fill();
        ctx.stroke();
        
        ctx.save();
        ctx.translate(
            canvas.width / 2 + Math.cos(currentAngle + arcSize / 2) * 100,
            canvas.height / 2 + Math.sin(currentAngle + arcSize / 2) * 100
        );
        ctx.rotate(currentAngle + arcSize / 2 + Math.PI / 2);
        ctx.fillStyle = "#333";
        ctx.font = "bold 16px sans-serif";
        ctx.fillText(sections[i], -ctx.measureText(sections[i]).width / 2, 0);
        ctx.restore();

        currentAngle += arcSize;
    }
}

function spinWheel() {
    spinAngle = Math.random() * 1000 + 2000; // Definir una rotación aleatoria
    let angle = spinAngle;
    const spinInterval = setInterval(() => {
        currentAngle += angle * 0.01;
        angle *= 0.98; // Desacelerar gradualmente
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        drawWheel();
        if (angle <= 0.05) clearInterval(spinInterval); // Detener la ruleta
    }, 16);
}

drawWheel();
spinButton.addEventListener("click", spinWheel);