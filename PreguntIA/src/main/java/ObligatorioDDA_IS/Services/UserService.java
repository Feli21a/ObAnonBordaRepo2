package ObligatorioDDA_IS.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.DTO.UserRegistrationDTO;
import ObligatorioDDA_IS.Models.User;
import ObligatorioDDA_IS.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RankingSystem rankingSystem; // Inyectar RankingSystem

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("El correo ya está en uso");
        }
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        // Agregar el usuario al ranking
        rankingSystem.updateRanking(user);

        return userRepository.save(user);
    }

    public User authenticateUser(String email, String pass, HttpSession session) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(pass, user.getPassword())) {
            throw new RuntimeException("Correo o contraseña incorrectos");
        }

        // Asegúrate de que el usuario esté en el ranking
        rankingSystem.updateRanking(user);

        // Establecer el usuario autenticado en la sesión
        session.setAttribute("user", user);

        return user;
    }

    public Map<String, Object> getUserProfile(HttpSession session) {
        // Obtener al usuario autenticado desde la sesión
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        // Construir el perfil del usuario
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("id", loggedInUser.getUserId());
        profileData.put("username", loggedInUser.getUsername());
        profileData.put("avatar", loggedInUser.getAvatar() != null ? loggedInUser.getAvatar() : "/img/AvatarDefault.png");
        profileData.put("maxScoreSP", loggedInUser.getMaxScoreSP());
        profileData.put("totalCorrectQuestions", loggedInUser.getTotalScore());

        return profileData;
    }

    @Transactional
    public void updateAvatar(String avatar, HttpSession session) {
        // Validar que el usuario esté autenticado
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        // Actualizar el avatar del usuario
        loggedInUser.setAvatar(avatar);

        // Guardar los cambios en la base de datos
        userRepository.save(loggedInUser);

        // Actualizar la sesión con los datos actualizados del usuario
        session.setAttribute("user", loggedInUser);

        System.out.println("Avatar actualizado correctamente para el usuario: " + loggedInUser.getUsername());
    }

    @Transactional
    public void updateUserScore(Map<String, Integer> scoreData, HttpSession session) {
        // Obtener al usuario autenticado desde la sesión
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        int newCorrectAnswers = scoreData.get("newCorrectAnswers");

        // Recuperar al usuario actualizado desde la base de datos
        User userFromDb = userRepository.findById(loggedInUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar el puntaje total y el puntaje máximo
        userFromDb.setTotalScore(userFromDb.getTotalScore() + newCorrectAnswers);
        if (newCorrectAnswers > userFromDb.getMaxScoreSP()) {
            userFromDb.setMaxScoreSP(newCorrectAnswers);
        }

        // Guardar los cambios en la base de datos
        userRepository.save(userFromDb);

        // Actualizar el ranking
        rankingSystem.updateRanking(userFromDb); // Llamada con datos actualizados

        // Actualizar la sesión
        session.setAttribute("user", userFromDb);
    }

    public void updateUsername(String newUsername, HttpSession session) {
        User currentUser = (User) session.getAttribute("user"); // Cambiado a "user"
        if (currentUser == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        if (newUsername == null || newUsername.isBlank()) {
            throw new RuntimeException("El nombre de usuario no puede estar vacío");
        }
        currentUser.setUsername(newUsername);
        userRepository.save(currentUser);
    }

    public void updatePassword(String newPassword, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new RuntimeException("La contraseña no puede estar vacía");
        }

        // Actualizar la contraseña encriptada
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);

        // Invalidar la sesión actual
        session.invalidate();
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
