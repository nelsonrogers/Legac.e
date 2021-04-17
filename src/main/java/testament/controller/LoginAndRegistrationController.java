package testament.controller;

import org.springframework.mail.SimpleMailMessage;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;



import javax.validation.Valid;

@Controller
public class LoginAndRegistrationController {
    private final UserService userService;

    private final SecurityService securityService;

    private final UserValidator userValidator;

    @Autowired
    private JavaMailSender javaMailSender;

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

        String email = userForm.getEmail();

        envoiMail(email);
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
    public String envoiMail(String email) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email, email);

        msg.setSubject("Bienvenue sur Legac.e");
        msg.setText("Cher utilisateur,\n" +
                "Merci de faire confiance à Legac.e pour gérer vos données numériques quand vous ne serez plus là. \n" +
                "Vous pouvez maintenant vous connecter en tant qu’utilisateur sur www.legac.e.com afin d’accéder à vos informations personnelles, vos volontés et la configuration de votre page souvenir.\n" +
                "Ce n’était pas vous ? Merci d’envoyer un mail à Legac.etest@gmail.com.\n" +
                "L’équipe Legac.e \n");

        javaMailSender.send(msg);
        return "espaceUtilisateur";
    }
}