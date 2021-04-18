/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.controller;

import javax.mail.MessagingException;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import testament.entity.ServiceReseau;

/**
 *
 * @author nelsonrogers
 */


public class serviceReseauController {
    
    
    @PostMapping("/preferenceReseaux")
    public String registration(@Valid @ModelAttribute("userService") ServiceReseau userService, BindingResult bindingResult) {



        //userService.
        /*envoiMail(email);
        if (userForm.isTestament()) {
            envoiTestament(email);
        }*/

        return "redirect:/welcome";

    }
}
