package ObligatorioDDA_IS.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ObligatorioDDA_IS.Models.RankingSystem;
import ObligatorioDDA_IS.Models.User;
import ObligatorioDDA_IS.Services.UserService;
import jakarta.annotation.PostConstruct;

/**
 * Controlador para gestionar el ranking de los usuarios.
 */
@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    @Autowired
    private RankingSystem rankingSystem;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initializeRanking() {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            rankingSystem.updateRanking(user);
        }
    }

    // Actualizar el ranking de un usuario
    @PostMapping("/update")
    public ResponseEntity<String> updateRanking(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("userId");

        try {
            User user = userService.getUserById(userId); // Obtener usuario del servicio
            rankingSystem.updateRanking(user); // Actualizar el ranking
            return ResponseEntity.ok("Ranking actualizado");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Obtener el ranking completo
    @GetMapping("/list")
    public ResponseEntity<List<User>> getRanking() {
        return ResponseEntity.ok(rankingSystem.getRanking());
    }
}
