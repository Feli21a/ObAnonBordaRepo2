// gameHandler.js

// Iniciar partida con dificultad seleccionada
async function startGame(difficulty) {
    try {
        const response = await fetch('/game/start-singleplayer', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ difficulty: difficulty })
        });
        const data = await response.json();
        if (response.ok) {
            console.log('Game started:', data);
            sessionStorage.setItem('gameId', data.gameId);
            sessionStorage.setItem('difficulty', difficulty); // Guarda la dificultad
            fetchQuestion();
        } else {
            console.error('Error starting game:', data);
        }
    } catch (error) {
        console.error('Error en startGame:', error);
    }
}

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
            console.log('Question fetched:', data);
            showQuestionModal(data.question, data.options);
        } else {
            console.error('Error fetching question:', data);
        }
    } catch (error) {
        console.error('Error en fetchQuestion:', error);
    }
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

