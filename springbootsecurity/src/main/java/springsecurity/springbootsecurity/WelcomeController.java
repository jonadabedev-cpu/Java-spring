package springsecurity.springbootsecurity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public String welcome(Authentication authentication) {
        if (authentication == null) {
            return "Bem-vindo! Faça login para continuar.";
        }
        return "Bem-vindo, " + authentication.getName() + "!";
    }

    @GetMapping("/user")
    public String userArea(Authentication authentication) {
        return "Área do usuário | Logado como: " + authentication.getName();
    }

    @GetMapping("/managers")
    public String managersArea(Authentication authentication) {
        return "Área dos managers | Logado como: " + authentication.getName();
    }

    @GetMapping("/admin")
    public String adminArea(Authentication authentication) {
        return "Área do admin | Logado como: " + authentication.getName();
    }

    @GetMapping("/logout-link")
    public String logoutLink() {
        return "<a href='/logout'>Clique aqui para sair</a>";
    }
}
