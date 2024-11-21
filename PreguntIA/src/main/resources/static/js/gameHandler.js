// gameHandler.js
document.addEventListener("DOMContentLoaded", function () {
    // Cargar datos del jugador (nombre y avatar)
    fetch("/api/users/perfil")
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al obtener el perfil del usuario");
            }
            return response.json();
        })
        .then(data => {
            const { username, avatar } = data;

            // Configurar el nombre del usuario
            document.getElementById("playerName").textContent = username || "Jugador";

            // Configurar el avatar solo si es válido
            const avatarElement = document.getElementById("playerAvatar");
            if (avatar) {
                avatarElement.src = avatar; // Usa la URL desde la base de datos
            } else {
                avatarElement.src = "/img/Avatar1.png"; // Fallback
            }
        })
        .catch(error => console.error("Error al cargar el perfil del jugador:", error));
});



// Obtener una nueva pregunta desde la API después de girar la ruleta
async function fetchQuestionFromAPI(category) {
    const gameId = sessionStorage.getItem('gameId');
    try {
        const response = await fetch(`/question/fetch?gameId=${gameId}&category=${encodeURIComponent(category)}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });
        const data = await response.json();

        if (response.ok) {
            console.log("Datos recibidos de la API:", data);

            // Validar que los datos estén completos
            if (!data.question || !data.options || !data.correctAnswer) {
                console.error("Error: La API no devolvió los datos esperados.", data);
                alert("No se pudo obtener la pregunta correctamente. Por favor, intenta más tarde.");
                return;
            }

            // Llamar al modal con los datos correctos
            showQuestionModal(data.question, data.options, data.correctAnswer);
        } else {
            console.error("Error al obtener la pregunta:", data);
            alert("Error al obtener la pregunta. Intenta nuevamente.");
        }
    } catch (error) {
        console.error("Error en fetchQuestionFromAPI:", error);
        alert("Error de conexión. Por favor, revisa tu conexión a Internet.");
    }
}

// Obtener una nueva pregunta basada en la dificultad y categoría almacenada
async function fetchQuestion(category = 'Science') {
    const gameId = sessionStorage.getItem('gameId');
    const difficulty = sessionStorage.getItem('difficulty');
    try {
        const response = await fetch(`/question/fetch?gameId=${gameId}&category=${category}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });
        const data = await response.json();
        if (response.ok) {
            console.log('Pregunta obtenida:', data);
            showQuestionModal(data.question, data.options);
        } else {
            console.error('Error al obtener la pregunta:', data);
        }
    } catch (error) {
        console.error('Error en fetchQuestion:', error);
    }
}

//SONIDO MODAL
// Definición global del sonido para los modales
const modalOpenSound = new Audio('/audio/movimiento.mp3');

// Función para reproducir el sonido al abrir un modal
function playModalOpenSound() {
    modalOpenSound.currentTime = 0; // Reinicia el sonido
    modalOpenSound.play().catch(error => {
        console.error("Error al reproducir el sonido del modal:", error);
    });
}

// Asociar el sonido al modal de categoría y preguntas
document.addEventListener('DOMContentLoaded', () => {
    const categoryModal = document.getElementById('categoryModal');
    const questionModal = document.getElementById('questionModal');

    // Asociar eventos al modal de categoría
    if (categoryModal) {
        categoryModal.addEventListener('shown.bs.modal', playModalOpenSound);
    } else {
        console.error("No se encontró el modal con ID 'categoryModal'.");
    }

    // Asociar eventos al modal de preguntas
    if (questionModal) {
        questionModal.addEventListener('shown.bs.modal', playModalOpenSound);
    } else {
        console.error("No se encontró el modal con ID 'questionModal'.");
    }
});

function showCategoryModal(category) {
    const selectedCategory = data.find(item => item.label === category);
    if (!selectedCategory) return;

    const categoryImage = document.getElementById("categoryImage");
    const categoryName = document.getElementById("categoryName");
    const categoryZoomContainer = document.getElementById("categoryZoomContainer");

    // Configura la imagen y el nombre de la categoría
    categoryImage.src = selectedCategory.image;
    categoryName.textContent = category;

    // Muestra el modal de categoría y aplica la animación de zoom al contenedor
    const categoryModal = document.getElementById("categoryModal");
    categoryModal.style.display = "flex";
    categoryZoomContainer.classList.add("zoom");
    playModalOpenSound(); // Reproduce el sonido al abrir el modal

    // Después de 2 segundos, cambia la animación a "alejamiento" y oculta el modal luego de 1 segundo
    setTimeout(() => {
        categoryZoomContainer.classList.remove("zoom");
        categoryZoomContainer.classList.add("shrink"); // Aplica la clase de alejamiento

        // Inicia la generación de la pregunta mientras se reproduce el efecto de alejamiento
        fetchQuestionFromAPI(category);

        // Oculta el modal de categoría tras el efecto de alejamiento
        setTimeout(() => {
            categoryZoomContainer.classList.remove("shrink"); // Restablece el tamaño del contenedor
            categoryModal.style.display = "none"; // Oculta el modal de categoría
        }, 1000); // Tiempo para el efecto de alejamiento
    }, 2000); // Duración de la animación de zoom en milisegundos
}

// Ruta del sonido del temporizador
const timerSound = new Audio('/audio/timer.mp3');
timerSound.loop = true; // El sonido del temporizador se repetirá mientras el temporizador esté activo

// Función para iniciar el temporizador con efecto de sonido
function startTimer(onTimeout) {
    const timerElement = document.getElementById("timer");
    let timeLeft = 10; // Tiempo en segundos

    // Iniciar el sonido del temporizador
    timerSound.play().catch(error => console.error("Error reproduciendo el sonido del temporizador:", error));

    timerElement.textContent = `${timeLeft} segundos`;

    timer = setInterval(() => {
        timeLeft -= 1;
        timerElement.textContent = `${timeLeft} segundos`;

        if (timeLeft <= 0) {
            clearInterval(timer); // Detener el temporizador
            timerSound.pause(); // Detener el sonido del temporizador
            timerSound.currentTime = 0; // Reiniciar el sonido para el futuro
            playTimeUpSound(); // Reproducir un sonido de tiempo agotado
            onTimeout(); // Ejecutar la acción al terminar el tiempo
        }
    }, 1000);
}

// Reproducir sonido cuando el tiempo se agota
function playTimeUpSound() {
    const timeUpSound = new Audio('/audio/timeUp.mp3'); // Asegúrate de tener este archivo de sonido
    timeUpSound.play().catch(error => console.error("Error reproduciendo el sonido de tiempo agotado:", error));
}

// Mostrar la pregunta y opciones en el modal
function showQuestionModal(questionText, options, correctAnswer) {
    document.getElementById("modal-question-text").textContent = questionText || "Pregunta no disponible";

    const sanitizedOptions = options.map(option => option.replace(/"/g, ""));
    const sanitizedCorrectAnswer = correctAnswer.trim();

    const modalOptions = document.querySelectorAll(".answer-button");
    modalOptions.forEach((button, index) => {
        if (sanitizedOptions[index]) {
            button.textContent = sanitizedOptions[index];
            button.style.display = "inline-block";
            button.style.backgroundColor = ""; // Restablecer color de fondo
            button.style.color = ""; // Restablecer color del texto
            button.disabled = false; // Habilitar el botón

            playModalOpenSound(); // Reproduce el sonido al abrir el modal

            button.onclick = () => {
                clearTimeout(timer); // Detener el temporizador al seleccionar una respuesta
                timerSound.pause(); // Detener el sonido del temporizador
                timerSound.currentTime = 0; // Reiniciar el sonido para el futuro

                console.log("Opción seleccionada:", sanitizedOptions[index]);
                console.log("Respuesta correcta:", sanitizedCorrectAnswer);

                if (sanitizedOptions[index].trim().toLowerCase() === sanitizedCorrectAnswer.trim().toLowerCase()) {
                    console.log("Respuesta correcta detectada");
                    button.style.backgroundColor = "green";
                    button.style.color = "white";
                    button.classList.add("correct-answer");

                    playCorrectAnswerSound();
                    updateScore();

                    setTimeout(() => {
                        closeModal();
                        document.getElementById("spinButton").disabled = false;
                        console.log("Listo para girar de nuevo.");
                    }, 1000);
                } else {
                    console.log("Respuesta incorrecta detectada");
                    button.style.backgroundColor = "red";
                    button.style.color = "white";

                    playIncorrectAnswerSound(); // Reproducir sonido de respuesta incorrecta

                    // Resaltar la respuesta correcta en verde
                    modalOptions.forEach((btn, idx) => {
                        if (sanitizedOptions[idx].trim().toLowerCase() === sanitizedCorrectAnswer.toLowerCase()) {
                            btn.style.backgroundColor = "green";
                            btn.style.color = "white";
                        }
                    });

                    // Esperar 3     segundos antes de finalizar el juego
                    setTimeout(() => {
                        const finalScore = document.getElementById("correctAnswers").textContent;
                        endGame(finalScore, "incorrect"); // Mostrar el modal de fin de partida con el puntaje final
                    }, 3000);
                }

                modalOptions.forEach(btn => btn.disabled = true); // Desactivar todos los botones
            };
        } else {
            button.style.display = "none"; // Ocultar botones no utilizados
        }
    });

    // Mostrar el modal de pregunta
    document.getElementById("questionModal").style.display = "flex";
    console.log("Modal de pregunta mostrado");

    // Iniciar el temporizador con efecto de sonido
    startTimer(() => {
        console.log("Tiempo agotado. Partida finalizada.");
        const finalScore = document.getElementById("correctAnswers").textContent;
        endGame(finalScore, "incorrect"); // Finalizar partida como incorrecta
    });
}

// Detener el temporizador y su sonido al cerrar el modal
function closeModal() {
    clearInterval(timer); // Detener el temporizador si el modal se cierra
    timerSound.pause(); // Detener el sonido del temporizador
    timerSound.currentTime = 0; // Reiniciar el sonido para el futuro
    document.getElementById("questionModal").style.display = "none";
}

// Enviar la respuesta seleccionada
async function submitAnswer(selectedAnswer, buttonElement) {
    const gameId = sessionStorage.getItem('gameId');
    try {
        const response = await fetch(`/answer/submit?gameId=${gameId}&answer=${encodeURIComponent(selectedAnswer)}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });
        const data = await response.json();

        if (response.ok) {
            if (data.isCorrect) {
                // Respuesta correcta
                buttonElement.style.backgroundColor = "green";
                buttonElement.style.color = "white";
                alert("¡Respuesta correcta!");
                updateScore(data.score);
            } else {
                // Respuesta incorrecta
                buttonElement.style.backgroundColor = "red";
                buttonElement.style.color = "white";
                alert("Respuesta incorrecta.");
            }
        } else {
            console.error("Error al enviar la respuesta:", data);
        }
    } catch (error) {
        console.error("Error en submitAnswer:", error);
    }
}

// Actualizar el puntaje en la interfaz y sincronizar con el backend
function updateScore() {
    const correctAnswersElement = document.getElementById("correctAnswers");

    let currentCount = parseInt(correctAnswersElement.textContent, 10) || 0; // Aciertos en la partida actual
    currentCount += 1; // Incrementar al responder correctamente
    correctAnswersElement.textContent = currentCount;

    console.log("Incrementado el contador de respuestas correctas localmente. Nuevo valor:", currentCount);
}

// Finalizar el juego y mostrar resultados
async function endGame(finalScore, status) {
    const gameId = sessionStorage.getItem('gameId');
    const endGameModal = document.getElementById("endGameModal");

    // Mostrar el puntaje final en el modal
    const finalScoreElement = document.getElementById("finalScore");
    if (finalScoreElement) {
        finalScoreElement.textContent = finalScore;
    }

    // Mostrar el modal de fin de juego
    if (endGameModal) {
        endGameModal.style.display = "flex";
    }

    // Reproduce el sonido dependiendo del estado del juego
    if (status === "incorrect") {
        playIncorrectAnswerSound();
    } else {
        playCorrectAnswerSound();
    }

    // Enviar puntaje final al backend
    try {
        const response = await fetch(`/spgame/${gameId}/end`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ finalScore }) // Enviar solo el puntaje acumulado
        });

        if (response.ok) {
            console.log("Juego finalizado y puntaje actualizado en el backend.");
            await refreshUserProfile(); // Refresca el perfil después de finalizar el juego
        } else {
            console.error("Error al finalizar el juego en el backend.");
        }
    } catch (error) {
        console.error("Error al intentar finalizar el juego:", error);
    }
}

async function refreshUserProfile() {
    try {
        const response = await fetch("/api/users/perfil");
        if (!response.ok) throw new Error("Error al obtener el perfil del usuario");

        const data = await response.json();
        document.getElementById("usernameDisplay").textContent = data.username || "Sin Nombre";
        document.getElementById("currentAvatar").src = data.avatar || "/img/Avatar1.png";
        document.getElementById("maxScoreSPDisplay").textContent = data.maxScoreSP || "0";
    } catch (error) {
        console.error("Error al actualizar el perfil del usuario:", error);
    }
}

async function replayGame() {
    try {
        // Obtener la dificultad de la partida actual desde sessionStorage
        const difficulty = sessionStorage.getItem('difficulty');
        if (!difficulty) {
            console.error("Error: No se encontró dificultad en sessionStorage.");
            alert("Error: No se puede reiniciar la partida. Por favor, vuelve al menú.");
            window.location.href = "/menu"; // Redirigir al menú en caso de error
            return;
        }

        // Hacer una nueva solicitud para iniciar el juego con la misma dificultad
        const response = await fetch(`/spgame/start?difficulty=${difficulty}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });

        if (!response.ok) throw new Error("Error al reiniciar el juego");

        const data = await response.json();
        console.log("Respuesta del servidor (reinicio):", data);

        if (data.gameId) {
            // Actualizar sessionStorage con el nuevo gameId
            sessionStorage.setItem('gameId', data.gameId);

            // Reiniciar la interfaz
            document.getElementById("correctAnswers").textContent = "0";
            document.getElementById("endGameModal").style.display = "none";

            // Redirigir a la pantalla de la ruleta con el nuevo gameId
            window.location.href = `/ruleta?gameId=${data.gameId}&difficulty=${difficulty}`;
        } else {
            console.error("Error: `gameId` no se recibió en la respuesta del servidor al reiniciar.");
        }
    } catch (error) {
        console.error("Error en replayGame:", error);
        alert("Hubo un problema al reiniciar la partida. Por favor, intenta más tarde.");
    }
}


function goToMenu() {
    window.location.href = "/menu"; // Replace with the actual URL of your menu
}

document.addEventListener('DOMContentLoaded', () => {
    const gameId = sessionStorage.getItem('gameId');
    if (!gameId) {
        alert("No se encontró un ID de juego activo. Volviendo al menú.");
        window.location.href = "/menu"; // Ajusta la URL del menú según corresponda
    }
}
);

function playCorrectAnswerSound() {
    const correctAnswerSound = new Audio('/audio/respuestaCorrecta.mp3');
    correctAnswerSound.currentTime = 0;
    correctAnswerSound.play().catch(error => {
        console.error("Error playing correct answer sound:", error);
    });
}

function playIncorrectAnswerSound() {
    const incorrectAnswerSound = new Audio('/audio/respuestaIncorrecta.mp3');
    incorrectAnswerSound.currentTime = 0;
    incorrectAnswerSound.play().catch(error => {
        console.error("Error playing incorrect answer sound:", error);
    });
}

