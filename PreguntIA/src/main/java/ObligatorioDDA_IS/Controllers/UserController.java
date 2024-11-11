package ObligatorioDDA_IS.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
            User loggedInUser = usuarioService.authenticateUser(user.getEmail(), user.getPassword());
            session.setAttribute("user", loggedInUser); // Agrega el usuario a la sesión
            return ResponseEntity.ok().body("/menu"); // Redirige a la página de menú
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<String> getUserProfile(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser != null) {
            return ResponseEntity.ok(loggedInUser.getUsername()); // O cualquier atributo que quieras mostrar
        } else {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión actual
        return ResponseEntity.ok("Sesión cerrada con éxito");
    }
}