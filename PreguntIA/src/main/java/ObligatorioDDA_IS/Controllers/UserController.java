package ObligatorioDDA_IS.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Map<String, String>> authenticateUser(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {
        try {
            // Autenticar al usuario y agregarlo a la sesión
            User loggedInUser = userService.authenticateUser(email, password, session);

            // Construir la respuesta JSON
            Map<String, String> response = new HashMap<>();
            response.put("userId", String.valueOf(loggedInUser.getUserId())); // Devolver el userId
            response.put("location", "/menu"); // Indicar la redirección

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Enviar un error en formato JSON
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<Map<String, Object>> getUserProfile(HttpSession session) {
        try {
            // Delegar la lógica al servicio
            Map<String, Object> profileData = userService.getUserProfile(session);
            return ResponseEntity.ok(profileData);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/update-username")
    public ResponseEntity<String> updateUsername(@RequestBody Map<String, String> payload, HttpSession session) {
        try {
            userService.updateUsername(payload.get("username"), session);
            return ResponseEntity.ok("Nombre de usuario actualizado");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> payload, HttpSession session) {
        try {
            String newPassword = payload.get("password");
            userService.updatePassword(newPassword, session);
            return ResponseEntity.ok("Contraseña actualizada, cierre de sesión requerido.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-score")
    public ResponseEntity<String> updateScore(@RequestBody Map<String, Integer> scoreData, HttpSession session) {
        try {
            // Delegar toda la lógica al servicio
            userService.updateUserScore(scoreData, session);
            return ResponseEntity.ok("Puntaje actualizado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el puntaje: " + e.getMessage());
        }
    }

    @PostMapping("/update-avatar")
    public ResponseEntity<String> updateAvatar(@RequestBody Map<String, String> avatarData, HttpSession session) {
        try {
            if (!avatarData.containsKey("avatar")) {
                return ResponseEntity.badRequest().body("Falta el campo 'avatar' en la solicitud.");
            }

            // Delegar la lógica al servicio
            userService.updateAvatar(avatarData.get("avatar"), session);
            return ResponseEntity.ok("Avatar actualizado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el avatar");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión actual
        return ResponseEntity.ok("Sesión cerrada con éxito");
    }

}