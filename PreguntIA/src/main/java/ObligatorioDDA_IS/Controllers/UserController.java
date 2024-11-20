package ObligatorioDDA_IS.Controllers;

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
import ObligatorioDDA_IS.Services.UserService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

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
            User authenticatedUser = userService.authenticateUser(user.getEmail(), user.getPassword());
            session.setAttribute("user", authenticatedUser);
            System.out.println("Usuario autenticado y guardado en la sesión: " + authenticatedUser.getUsername());
            return ResponseEntity.ok("/menu");
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> getUserProfile(HttpSession session) {
        try {
            return ResponseEntity.ok(userService.getUserProfile(session));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/update-avatar")
    public ResponseEntity<String> updateAvatar(@RequestBody Map<String, String> avatarData, HttpSession session) {
        try {
            userService.updateAvatar(avatarData.get("avatar"), session);
            return ResponseEntity.ok("Avatar actualizado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-score")
    public ResponseEntity<String> updateMaxScoreSP(@RequestBody Map<String, Integer> scoreData, HttpSession session) {
        try {
            userService.updateMaxScoreSP(scoreData.get("maxScoreSP"), session);
            return ResponseEntity.ok("MaxScoreSP actualizado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        userService.logout(session);
        return ResponseEntity.ok("Sesión cerrada con éxito");
    }
}
