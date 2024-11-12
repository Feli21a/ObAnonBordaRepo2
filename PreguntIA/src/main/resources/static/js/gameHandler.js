// Iniciar partida con dificultad seleccionada
async function startGame(difficulty) {
    try {
        const response = await fetch('/game/start-singleplayer', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({ difficulty: difficulty })
        });
        const data = await response.json();

        if (response.ok) {
            console.log('Game started:', data);
            // Guarda el gameId para futuras solicitudes
            sessionStorage.setItem('gameId', data.gameId);
            // Actualiza la interfaz para mostrar la primera pregunta
            fetchQuestion();
        } else {
            console.error('Error starting game:', data);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}
// Obtener una nueva pregunta
// Obtener una nueva pregunta con la dificultad almacenada
async function fetchQuestion() {
    const gameId = sessionStorage.getItem('gameId');
    const difficulty = sessionStorage.getItem('difficulty'); // Obtener dificultad
    const category = 'Science'; // O la categoría que seleccione el usuario

    try {
        const response = await fetch(`/game/fetch-question?gameId=${gameId}&category=${category}&difficulty=${difficulty}`, {
            method: 'POST'
        });
        const data = await response.json();

        if (response.ok) {
            console.log('Question fetched:', data);
            displayQuestion(data.question);
        } else {
            console.error('Error fetching question:', data);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}


// Mostrar la pregunta en la interfaz
function displayQuestion(question) {
    document.getElementById('questionText').innerText = question.questionText;
    const optionsContainer = document.getElementById('optionsContainer');
    optionsContainer.innerHTML = '';

    question.options.forEach(option => {
        const optionButton = document.createElement('button');
        optionButton.innerText = option;
        optionButton.onclick = () => submitAnswer(option);
        optionsContainer.appendChild(optionButton);
    });
}
// Enviar la respuesta seleccionada
async function submitAnswer(selectedAnswer) {
    const gameId = sessionStorage.getItem('gameId');

    try {
        const response = await fetch(`/game/submit-answer?gameId=${gameId}&answer=${selectedAnswer}`, {
            method: 'POST'
        });
        const data = await response.json();

        if (response.ok) {
            if (data.isCorrect) {
                console.log('Correct answer!');
                updateScore(data.score); // Actualizar el puntaje en la interfaz
                fetchQuestion(); // Obtener una nueva pregunta
            } else {
                console.log('Incorrect answer. Game over!');
                endGame(data.score, data.status);
            }
        } else {
            console.error('Error submitting answer:', data);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Función para actualizar el puntaje en la interfaz
function updateScore(score) {
    document.getElementById('score').innerText = `Score: ${score}`;
}

// Función para finalizar el juego y mostrar resultados
function endGame(score, status) {
    document.getElementById('gameStatus').innerText = `Game Over - ${status}`;
    document.getElementById('finalScore').innerText = `Final Score: ${score}`;
}
