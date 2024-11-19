package ObligatorioDDA_IS.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ObligatorioDDA_IS.DTO.UserRegistrationDTO;
import ObligatorioDDA_IS.Models.User;
import ObligatorioDDA_IS.Repository.UserRepository;
import ObligatorioDDA_IS.Services.UserService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@ModelAttribute UserRegistrationDTO user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Registro exitoso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@ModelAttribute UserRegistrationDTO user, HttpSession session) {
        try {
            User loggedInUser = userService.authenticateUser(user.getEmail(), user.getPassword());
            session.setAttribute("user", loggedInUser); // Agrega el usuario a la sesión
            return ResponseEntity.ok().body("/menu"); // Redirige a la página de menú
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> getUserProfile(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser != null) {
            // Reflejar el valor actualizado del perfil
            Map<String, Object> profileData = new HashMap<>();
            profileData.put("username", loggedInUser.getUsername());
            profileData.put("maxScoreSP", loggedInUser.getMaxScoreSP());
            profileData.put("avatar",
                    loggedInUser.getAvatar() != null ? loggedInUser.getAvatar() : "/img/MundiTriste.png");
            return ResponseEntity.ok(profileData);
        } else {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }
    }

    @PostMapping("/update-avatar")
    public ResponseEntity<String> updateAvatar(@RequestBody Map<String, String> avatarData, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser != null && avatarData.containsKey("avatar")) {
            // Actualizar el avatar en el objeto del usuario
            loggedInUser.setAvatar(avatarData.get("avatar"));

            // Guardar los cambios en la base de datos (o repositorio)
            userRepository.save(loggedInUser);

            return ResponseEntity.ok("Avatar actualizado correctamente");
        } else {
            return ResponseEntity.status(400).body("No se pudo actualizar el avatar");
        }
    }

    @PostMapping("/update-score")
    public ResponseEntity<String> updateMaxScoreSP(HttpSession session, @RequestBody Map<String, Integer> scoreData) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser != null && scoreData.containsKey("maxScoreSP")) {
            int newScore = scoreData.get("maxScoreSP");

            // Actualiza solo si el nuevo puntaje es mayor
            if (newScore > loggedInUser.getMaxScoreSP()) {
                loggedInUser.setMaxScoreSP(newScore);
                userRepository.save(loggedInUser); // Guarda los cambios en la base de datos
            }

            return ResponseEntity.ok("MaxScoreSP actualizado correctamente");
        } else {
            return ResponseEntity.status(400).body("No se pudo actualizar el MaxScoreSP");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión actual
        return ResponseEntity.ok("Sesión cerrada con éxito");
    }
}