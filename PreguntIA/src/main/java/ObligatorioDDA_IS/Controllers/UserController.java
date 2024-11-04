package ObligatorioDDA_IS.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ObligatorioDDA_IS.DTO.UserRegistrationDTO;
import ObligatorioDDA_IS.Models.User;
import ObligatorioDDA_IS.Services.UserService;
import jakarta.validation.Valid;

//Controlador que expone los endpoints de registro y autenticación de usuarios.

@RestController
@RequestMapping("/api/usuarios")
public class UserController {
    @Autowired
    private UserService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO user) {
        User nuevoUsuario = usuarioService.registerUser(user);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestParam String email, @RequestParam String pass) {
        try {
            usuarioService.authenticateUser(email, pass);
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
