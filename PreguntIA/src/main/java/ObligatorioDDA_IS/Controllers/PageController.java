package ObligatorioDDA_IS.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ObligatorioDDA_IS.Models.User;
import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Spring buscará el archivo `register.html` en el directorio `templates`
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Spring buscará el archivo `register.html` en el directorio `templates`
    }

    @GetMapping("/ruleta")
    public String showRuletaPage() {
        return "ruleta";
    }

    @GetMapping("/menu")
    public String getMenuPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser != null) {
            model.addAttribute("username", loggedInUser.getUsername());
        }
        return "menu"; // nombre de la vista HTML
    }

}
