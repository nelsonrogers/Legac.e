/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.controller.twitter;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.TweetData;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.web.bind.annotation.ModelAttribute;
import testament.dao.UserRepository;
import testament.entity.Reseau;
import testament.entity.SocialConnection;
import testament.entity.Utilisateur;
import testament.entity.Volonte;

@Controller
@Slf4j
@Secured({ "ROLE_ADMIN", "ROLE_USER" }) // Réservé aux utilisateurs qui ont le rôle 'ROLE_USER' ou 'ROLE_ADMIN'
@RequestMapping(path = "/twitter")
public class PostTweetController {
    @Value("${spring.social.twitter.consumerKey}")
    String consumerKey;
    @Value("${spring.social.twitter.consumerSecret}")
    String consumerSecret;

    ConnectionRepository connectionRepository;
    
    @Autowired
    UserRepository utilisateurDAO;
    
    public PostTweetController(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    @GetMapping(path = "tweet")
    public String showTweetForm(Model model) {
        //String username;
        //model.addAttribute("username");
        //System.out.println(model.getAttribute("username"));
        // On montre le formulaire de tweet
        return "twitter/postTweet";
    }

    @PostMapping(path = "tweet")
    // TODO : bug sur updateStatus
    // public String postTweet(String message, RedirectAttributes redirectInfo) {
    public String reTweet(@AuthenticationPrincipal Utilisateur user, Long tweetId, RedirectAttributes redirectInfo) {//, @ModelAttribute("username") String username) {
        Twitter twitter = configureTwitter(); // user);
        String resultat;
        /*System.out.println(username);
        Utilisateur utilisateur = utilisateurDAO.findByUsername(username);
        List<Volonte> volontes = utilisateur.getVolontesUtilisateur();
        resultat = "Rien à retweeter";
        for (Volonte volonte : volontes) {
            if (volonte.getIdTweet() != null) {*/

        try {
            //Long tweetId = volonte.getIdTweet();
            // twitter.timelineOperations().updateStatus(message);
            twitter.timelineOperations().retweet(tweetId);
            /*twitter.directMessageOperations().sendDirectMessage("BaptisteVilled1", "salut c'est moi");
            twitter.timelineOperations().updateStatus("Salut comment va ?");*/
            resultat = "Le tweet a bien été posté";
        } catch (RuntimeException ex) {
            resultat = "Impossible de poster : " + ex.getMessage();
            // log.error("Unable to tweet {}", message, ex);
        } 
            //}
        //}
        
        // RedirectAttributes permet de transmettre des informations lors d'une
        // redirection,
        // Ici on transmet un message de succès ou d'erreur
        // Ce message est accessible et affiché dans la vue 'postTweet.html'
        redirectInfo.addFlashAttribute("resultat", resultat);
        return "redirect:/welcome"; // POST-Redirect-GET : on se redirige vers le formulaire de tweet
    }

    private Twitter configureTwitter() {  //Utilisateur user) {
        /*
        Utilisateur u = utilisateurDAO.getOne(user.getId());
        List<SocialConnection> socials = u.getSocialConnections();
            
            for (SocialConnection social : socials) {
                String providerId = social.getProviderId();
                String providerUserId = social.getProviderUserId();
                ConnectionKey key = new ConnectionKey(providerId, providerUserId);
                Connection<Twitter> connection = (Connection<Twitter>) connectionRepository.getConnection(key);
                Twitter twitter = connection.getApi();
                log.info("Création d'une API twitter pour {}", connection.getDisplayName());
                return twitter;
            }*/
        
        Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter.class);
        Twitter twitter = connection.getApi();
        
        return twitter;
    }

}
