package ObligatorioDDA_IS.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String showruletaPage() {
        return "ruleta"; // Spring buscará el archivo `register.html` en el directorio `templates`
    }
    
    
}
