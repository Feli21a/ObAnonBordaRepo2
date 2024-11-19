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
        // Obtén el usuario autenticado desde la sesión
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser != null) {
            // Crear un objeto de respuesta con los datos del perfil
            Map<String, Object> profileData = new HashMap<>();
            profileData.put("username", loggedInUser.getUsername());
            profileData.put("maxScoreSP", loggedInUser.getMaxScoreSP()); 
            profileData.put("avatar", loggedInUser.getAvatar()); // Asegúrate de que este campo esté configurado
            // Devolver la respuesta como JSON
            return ResponseEntity.ok(profileData);
        } else {
            // Devolver un estado 401 si el usuario no está autenticado
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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión actual
        return ResponseEntity.ok("Sesión cerrada con éxito");
    }
}