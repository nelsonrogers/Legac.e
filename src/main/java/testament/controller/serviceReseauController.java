/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.controller;

import javax.mail.MessagingException;
import javax.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import testament.entity.ServiceReseau;
import testament.entity.Utilisateur;

/**
 *
 * @author nelsonrogers
 */


public class serviceReseauController {
    
    
    @PostMapping("/preferenceReseaux")
    public String registration(@AuthenticationPrincipal Utilisateur user, 
            @Valid @ModelAttribute("userService") ServiceReseau userService, 
            BindingResult bindingResult) {
        
        userService.setUtilisateur(user);
        //userService.
        /*envoiMail(email);
        if (userForm.isTestament()) {
            envoiTestament(email);
        }*/

        return "redirect:/welcome";

    }
}
