// gameHandler.js
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
    let timeLeft = 15; // Tiempo en segundos

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

                    // Esperar 3 segundos antes de finalizar el juego
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

// Actualizar el puntaje en la interfaz
function updateScore() {
    const correctAnswersElement = document.getElementById("correctAnswers");

    // Convertir el texto actual a número y sumar 1
    let currentCount = parseInt(correctAnswersElement.textContent, 10) || 0;
    correctAnswersElement.textContent = currentCount + 1;

    console.log("Incrementado el contador de respuestas correctas. Nuevo valor:", currentCount + 1);
}

// Finalizar el juego y mostrar resultados
async function endGame(score, status) {
    const gameId = sessionStorage.getItem('gameId');
    const finalScoreElement = document.getElementById("finalScore");
    const endGameModal = document.getElementById("endGameModal");

    // Verifica que el elemento exista antes de modificarlo
    if (finalScoreElement) {
        finalScoreElement.textContent = score;
    } else {
        console.error("Elemento 'finalScore' no encontrado en el DOM.");
    }

    // Asegúrate de que el modal exista antes de mostrarlo
    if (endGameModal) {
        endGameModal.style.display = "flex";
    } else {
        console.error("Modal 'endGameModal' no encontrado en el DOM.");
    }

    // Reproduce el sonido correspondiente
    if (status === "incorrect") {
        playIncorrectAnswerSound();
    } else {
        playCorrectAnswerSound();
    }

    // Llamada al backend para actualizar el estado del juego a "Finalizada"
    try {
        const response = await fetch(`/spgame/${gameId}/end`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });

        if (!response.ok) {
            console.error("Error al finalizar la partida en el servidor.");
        }
    } catch (error) {
        console.error("Error en la conexión para finalizar la partida:", error);
    }
}

function replayGame() {
    // Reinicia el juego (resetear el puntaje, la ruleta, etc.)
    document.getElementById("correctAnswers").textContent = "0";
    document.getElementById("endGameModal").style.display = "none";
    // Aquí puedes agregar código adicional para reiniciar el estado del juego
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

