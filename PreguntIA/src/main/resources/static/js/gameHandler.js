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
            console.log('Pregunta obtenida:', data);
            showQuestionModal(data.question, data.options);
        } else {
            console.error('Error al obtener la pregunta:', data);
        }
    } catch (error) {
        console.error('Error en fetchQuestionFromAPI:', error);
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
        setTimeout(() => {
            categoryZoomContainer.classList.remove("shrink"); // Restablece el tamaño del contenedor
            categoryModal.style.display = "none"; // Oculta el modal de categoría
            fetchQuestionFromAPI(category); // Llama a la función para obtener la pregunta
        }, 1000); // Tiempo para el efecto de alejamiento
    }, 2000); // Duración de la animación de zoom en milisegundos (total: 3 segundos)
}



// Mostrar la pregunta y opciones en el modal
function showQuestionModal(questionText, options) {
    document.getElementById("modal-question-text").textContent = questionText || "Pregunta no disponible";
    const modalOptions = document.querySelectorAll(".answer-button");
    modalOptions.forEach((button, index) => {
        if (options[index]) {
            button.textContent = options[index];
            button.style.display = "inline-block";
            button.onclick = () => submitAnswer(options[index]);
        } else {
            button.style.display = "none";
        }
    });
    document.getElementById("questionModal").style.display = "flex";
}

// Enviar la respuesta seleccionada
async function submitAnswer(selectedAnswer) {
    const gameId = sessionStorage.getItem('gameId');
    try {
        const response = await fetch(`/answer/submit?gameId=${gameId}&answer=${encodeURIComponent(selectedAnswer)}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });
        const data = await response.json();
        if (response.ok) {
            if (data.isCorrect) {
                alert("¡Respuesta correcta!");
                updateScore(data.score);
                closeModal();
                fetchQuestion();
            } else {
                alert("Respuesta incorrecta. Juego terminado.");
                endGame(data.score, data.status);
                closeModal();
            }
        } else {
            console.error("Error en submitAnswer:", data);
        }
    } catch (error) {
        console.error('Error en submitAnswer:', error);
    }
}

// Actualizar el puntaje en la interfaz
function updateScore(score) {
    document.getElementById('correctAnswers').textContent = score;
}

// Finalizar el juego y mostrar resultados
function endGame(score, status) {
    document.getElementById("gameStatus").textContent = `Juego terminado - ${status}`;
    document.getElementById("finalScore").textContent = `Puntaje final: ${score}`;
    alert("Juego terminado. Puntaje final: " + score);
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
});

