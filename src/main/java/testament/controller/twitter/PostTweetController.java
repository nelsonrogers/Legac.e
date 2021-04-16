/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.controller.twitter;

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

    public PostTweetController(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    @GetMapping(path = "tweet")
    public String showTweetForm(Model model) {
        // On montre le formulaire de tweet
        return "twitter/postTweet";
    }

    @PostMapping(path = "tweet")
    // TODO : bug sur updateStatus
    // public String postTweet(String message, RedirectAttributes redirectInfo) {
    public String reTweet(Long tweetId, RedirectAttributes redirectInfo) {
        Twitter twitter = configureTwitter();
        String resultat;
        try {
            // twitter.timelineOperations().updateStatus(message);
            twitter.timelineOperations().retweet(tweetId);
            resultat = "Le tweet a bien été posté";
        } catch (RuntimeException ex) {
            resultat = "Impossible de poster : " + ex.getMessage();
            // log.error("Unable to tweet {}", message, ex);
        }
        // RedirectAttributes permet de transmettre des informations lors d'une
        // redirection,
        // Ici on transmet un message de succès ou d'erreur
        // Ce message est accessible et affiché dans la vue 'postTweet.html'
        redirectInfo.addFlashAttribute("resultat", resultat);
        return "redirect:tweet"; // POST-Redirect-GET : on se redirige vers le formulaire de tweet
    }

    private Twitter configureTwitter() {
        Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter.class);
        // Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret,
        // connection.createData().getAccessToken(),
        // connection.createData().getSecret());
        Twitter twitter = connection.getApi();
        log.info("Création d'une API twitter pour {}", connection.getDisplayName());
        return twitter;
    }

}
