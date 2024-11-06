// Cargar el sonido
const hoverSound = new Audio('audio/btnSound.mp3'); // Cambia 'btnSound.mp3' por el nombre de tu archivo
hoverSound.volume = 0.3;

// Función para reproducir sonido al pasar el mouse
function playHoverSound() {
    hoverSound.currentTime = 0; // Reinicia el sonido
    hoverSound.play().catch(error => {
        console.error("Error al reproducir el sonido: ", error);
    });
}

// Obtener todos los botones que deben tener el sonido
const buttons = document.querySelectorAll('button'); // Selecciona todos los botones en el documento

// Añadir el evento de mouseover a cada botón
buttons.forEach(button => {
    button.addEventListener('mouseover', playHoverSound);
});

/* Reproductor de Música Lofi
const lofiMusic = document.createElement('audio');
lofiMusic.src = 'audio/MusicaAmbienteMenu.mp3'; // Ruta relativa a la carpeta audio
lofiMusic.loop = true; // Reproduce en bucle
lofiMusic.volume = 0.8; // Establece el volumen al 50%
document.body.appendChild(lofiMusic); // Agrega el elemento de audio al body

// Función para iniciar la música al cargar la página
function playLofiMusic() {
    lofiMusic.play().catch(error => {
        console.log("Error al intentar reproducir música: ", error);
    });
}

// Llama a la función para reproducir música al cargar la página
document.addEventListener("DOMContentLoaded", () => {
    playLofiMusic();
}); 

*/