package ObligatorioDDA_IS.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.DTO.UserRegistrationDTO;
import ObligatorioDDA_IS.Models.User;
import ObligatorioDDA_IS.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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

        return userRepository.save(user);
    }

    public User authenticateUser(String email, String pass) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(pass, user.getPassword())) {
            throw new RuntimeException("Correo o contraseña incorrectos");
        }
        return user;
    }

    public Map<String, Object> getUserProfile(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("username", loggedInUser.getUsername());
        profileData.put("maxScoreSP", loggedInUser.getMaxScoreSP());
        profileData.put("avatar", loggedInUser.getAvatar() != null ? loggedInUser.getAvatar() : "/img/MundiTriste.png");

        return profileData;
    }

    public void updateAvatar(String avatar, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        loggedInUser.setAvatar(avatar);
        userRepository.save(loggedInUser);
    }

    public void updateMaxScoreSP(int newScore, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        if (newScore > loggedInUser.getMaxScoreSP()) {
            loggedInUser.setMaxScoreSP(newScore);
            userRepository.save(loggedInUser);
        }
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
}
