package testament.controller;

import lombok.extern.slf4j.Slf4j;
import testament.entity.Utilisateur;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping(path = "/user")
@Secured({"ROLE_ADMIN", "ROLE_USER"}) // Réservé aux utilisateurs qui ont le rôle 'ROLE_USER' ou 'ROLE_ADMIN'
public class UserController {
    @GetMapping(path = "pageUser")
    public String montrePageUtilisateur(
            @AuthenticationPrincipal Utilisateur user,  // Les infos de l'utilisateur connecté
            Model model) {
        log.info("L'utilisateur id: {}, email: {} accède à sa page", user.getId(), user.getEmail());
        return "pageUser"; // On affiche la vue 'pageUser.html'
    }
    
    @GetMapping(path = "pageSouvenir")
    public String souvenir(@AuthenticationPrincipal Utilisateur user, Model model) {
        return "pageSouvenir";
    }
    
    @GetMapping(path = "preferencesReseaux")
    public String preferenceReseaux(@AuthenticationPrincipal Utilisateur user, Model model) {
        return "preferencesReseaux";
    }
    
    @GetMapping(path = "informationUtilisateur")
    public String informationsUtilisateur(@AuthenticationPrincipal Utilisateur user, Model model) {
        return "informationsUtilisateur";
    }
    
}