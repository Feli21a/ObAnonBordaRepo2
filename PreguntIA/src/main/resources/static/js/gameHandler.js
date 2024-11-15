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
                    const finalScore = document.getElementById("correctAnswers").textContent;
                    endGame(finalScore, "incorrect"); // Mostrar el modal de fin de partida con el puntaje final
                }

                modalOptions.forEach(btn => btn.disabled = true); // Desactivar todos los botones
            };
        } else {
            button.style.display = "none"; // Ocultar botones no utilizados
        }
    });

    document.getElementById("questionModal").style.display = "flex";
    console.log("Modal de pregunta mostrado");
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
function updateScore(score) {
    const correctAnswersElement = document.getElementById("correctAnswers");

    // Convertir el texto actual a número y sumar 1
    let currentCount = parseInt(correctAnswersElement.textContent, 10) || 0;
    correctAnswersElement.textContent = currentCount + 1;

    console.log("Incrementado el contador de respuestas correctas. Nuevo valor:", currentCount + 1);
}

// Finalizar el juego y mostrar resultados
function endGame(score, status) {
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

// Cerrar el modal
function closeModal() {
    document.getElementById("questionModal").style.display = "none";
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

