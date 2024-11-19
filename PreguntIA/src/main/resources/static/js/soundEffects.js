// Cargar el sonido
const hoverSound = new Audio('audio/btnSound.mp3'); // Cambia 'btnSound.mp3' por la ruta de tu archivo de sonido

// Función para reproducir sonido al pasar el mouse
function playHoverSound(event) {
    // Ignorar si el evento se origina en un elemento <i>
    if (event.target.tagName.toLowerCase() === 'i') return;

    hoverSound.currentTime = 0; // Reinicia el sonido
    hoverSound.play().catch(error => {
        console.error("Error al reproducir el sonido: ", error);
    });
}

// Obtener todos los botones y avatares que deben tener el sonido
const hoverElements = document.querySelectorAll('button, .avatar-option'); // Incluye los botones y los avatares

// Añadir el evento de mouseover a cada elemento
hoverElements.forEach(element => {
    element.addEventListener('mouseover', playHoverSound);
});


// soundEffects.js


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