package testament.controller;

import testament.entity.Utilisateur;
import testament.service.SecurityService;
import testament.service.UserService;
import testament.validator.UserValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class LoginAndRegistrationController {
    private final UserService userService;

    private final SecurityService securityService;

    private final UserValidator userValidator;

    public LoginAndRegistrationController(UserService userService, SecurityService securityService, UserValidator userValidator) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new Utilisateur());
        System.out.println("salut");
        return "inscription";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("userForm") Utilisateur userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "inscription";
        }
        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/welcome";
    }

    /*Pourquoi quand on appelle ça dans la page login ça nous renvoie vers la page welcome ??*/
    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");

        if (logout != null)
            model.addAttribute("message", "Vous avez été déconnecté.");

        return "connexion";
    }
    
    
    /*@PostMapping("/espaceUtilisateur")
    public String accueil(@Valid @ModelAttribute("userForm") Utilisateur userForm, Model model, String error, String logout) {
        
        if (error != null)
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");

        if (logout != null)
            model.addAttribute("message", "Vous avez été déconnecté.");
        
        userService.save(userForm);
        
        securityService.autoLogin(userForm.getUsername(), userForm.getPassword());

        return "espaceUtilisateur";
    }*/
    
    @GetMapping("/esapceUtilisateur")
    public String conecte(Model model) {
        return "espaceUtilisateur";
    }
    
    @GetMapping({"/","/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }
    /*@GetMapping("/inscription")
    public String inscription(Model model) {
        return "connexion";
    }*/
    
    /*
    @GetMapping("/connexion")
    public String connexion(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");

        if (logout != null)
            model.addAttribute("message", "Vous avez été déconnecté.");

        return "espaceUtilisateur";
    }*/
}