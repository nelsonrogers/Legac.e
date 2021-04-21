/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.controller;

import javax.mail.MessagingException;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import testament.dao.UserRepository;
import testament.dao.VolonteRepository;
import testament.entity.Reseau;
import testament.entity.Volonte;
import testament.entity.Utilisateur;
import testament.service.VolonteService;

/**
 *
 * @author nelsonrogers
 */

@Controller 
@Slf4j
public class VolonteController {
    
    @Autowired
    VolonteRepository volonteDAO;
    
    @Autowired
    UserRepository utilisateurDAO;
    
    @GetMapping("/donnesTwitter")
    public String volonte(Model model) {
        model.addAttribute("volonte", new Volonte());
        return "donnesTwitter";
    }
    
    @PostMapping("/donnesTwitter")
    public String enregistrerVolontes(@AuthenticationPrincipal Utilisateur user, @Valid @ModelAttribute("volonte") Volonte volonte, 
            RedirectAttributes redirectInfo) {
        
        // resultat : message de succès ou d'erreur de l'enregistrement des volontés
        String resultat;
        try {
            // Si l'utilisateur a bien enregistré ses volontés twitter
            if (volonte.getIdTweet() != null || !volonte.getMessage().equals("") || !volonte.getUsernameDestinataire().equals("")) {
                
                Utilisateur u = utilisateurDAO.getOne(user.getId());
                volonte.setUtilisateur(u);
                volonte.setReseau(Reseau.TWITTER);

                // on enregistre les volontés dans la base de données
                volonteDAO.save(volonte);
                resultat = "Vos préférences Twitter ont bien été enregistrées";
            }
            else {
                resultat = "Vous n'avez rien enregistré";

            }
            
            
        } catch (Exception ex) {
            // Il y a eu un problème
            resultat = "Un problème est survenu : " + ex.getMessage();
        }
        // Message de succès ou d'erreur es affiché
        redirectInfo.addFlashAttribute("resultat", resultat);
        return "redirect:/welcome";

    }
}
