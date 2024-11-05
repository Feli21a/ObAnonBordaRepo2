package ObligatorioDDA_IS.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ObligatorioDDA_IS.DTO.UserRegistrationDTO;
import ObligatorioDDA_IS.Services.UserService;

// Controlador que expone los endpoints de registro y autenticación de usuarios.

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService usuarioService;

    // Usando @ModelAttribute para recibir el formulario completo
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@ModelAttribute UserRegistrationDTO user) {
        try {
            usuarioService.registerUser(user);
            ResponseEntity.ok("Registro exitoso");
            return ResponseEntity.ok().body("/login.html");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@ModelAttribute UserRegistrationDTO user) {
        try {
            usuarioService.authenticateUser(user.getEmail(), user.getPassword());
            return ResponseEntity.ok().body("/menu.html");

        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
