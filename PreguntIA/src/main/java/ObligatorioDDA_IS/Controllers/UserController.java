package ObligatorioDDA_IS.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
    private UserService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@ModelAttribute UserRegistrationDTO user) {
        try {
            usuarioService.registerUser(user);
            return ResponseEntity.ok("Registro exitoso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@ModelAttribute UserRegistrationDTO user, HttpSession session) {
        try {
            User loggedInUser = (User) usuarioService.authenticateUser(user.getEmail(), user.getPassword());
            session.setAttribute("user", loggedInUser); // Guarda el usuario en la sesión
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión actual
        return ResponseEntity.ok("Sesión cerrada con éxito");
    }
}