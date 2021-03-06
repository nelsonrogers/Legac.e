/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import testament.dao.UserRepository;
import testament.entity.SocialConnection;
import testament.entity.Utilisateur;
import testament.entity.Volonte;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

/**
 *
 * @author nelsonrogers
 */
@Controller
@Slf4j
//@RequestMapping("/scan")
public class ScannerController {
   

    @Autowired
    private UserRepository dao;
    
    private ArrayList<Utilisateur> utilisateurs; 
    
    /*
    @Autowired
    ConnectionRepository connectionRepository;
    
    @Value("${spring.social.twitter.consumerKey}")
    String consumerKey;
    @Value("${spring.social.twitter.consumerSecret}")
    String consumerSecret;
    */
    
    @GetMapping("/newScan")
    public String check() {
        return "pageAdmin";
    }
    
    // la m??thode scan met les infos des utilisateurs dans la m??me configuration 
    // que le fichier de personnes d??c??d??es
    @PostMapping("/newScan")
    public String scan(Model model) throws IOException {
        
        utilisateurs = new ArrayList<>();
        
        // On lit le fichier de personnes d??c??d??es
        try (FileReader fileReader = new FileReader("src/main/resources/static/deces-2021-m02.txt"); 
            BufferedReader reader = new BufferedReader(fileReader)) {
            
            // on r??cup??re les donn??es dans la variable data
            String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            

            // on parcoure l'ensemble des utilisateurs dans la base de donn??es
            for (Utilisateur personne : dao.findAll()) {
            
                /* 
                * on r??cup??re la date de naissance et on la configure pour 
                * correspondre au format sur le fichier de personnnes d??c??d??es
                */
                String date = personne.getDateNaiss().toString();
                String dateSimple = date.replace("-", "");
                // On supprime les espaces s'il y en a
                String nom = personne.getNom().replace(" ", "");
                String prenom = personne.getPrenom().replace(" ", "");
                String codeCommune = personne.getCodePostal().replace(" ","");
                String commune = personne.getCommuneNaiss().replace(" ","");
                
                
                // La personne a forc??ment un nom et un pr??nom
                String ligne = nom.toUpperCase() + "*" + prenom.toUpperCase();
                
                // si deuxi??me pr??nom
                if (personne.getPrenom2() != null) {
                    String prenom2 = personne.getPrenom2().replace(" ", "");
                    // on v??rifie que ce n'??tait pas juste des espaces vides
                    if (!prenom2.equals("")) {
                        ligne += " " + prenom2.toUpperCase();
                    }
                }
                
                // si troisi??me pr??nom
                if (personne.getPrenom3() != null) {
                    String prenom3 = personne.getPrenom3().replace(" ", "");
                    // on v??rifie que ce n'??tait pas juste des espaces vides
                    if (!prenom3.equals("")) {
                        ligne += " " + prenom3.toUpperCase();
                    }
                }
                
                // fin des pr??noms
                ligne += "/";


                int longueur = ligne.length();

                // on compl??te par des espaces jusqu'?? la position 81
                int nbEspaces = 80-longueur;
                for (int i=0 ; i < nbEspaces ; i++) {
                    ligne += " ";
                }

                // sexe : 1 ou 2, puis date de naissance, code commune, nom commune tous enchain??s
                ligne += personne.getSexe() + dateSimple + codeCommune + commune.toUpperCase();

                // si un utilisateur est d??c??d??, on l'ajoute ?? la liste 
                if(data.contains(ligne)) {
                    utilisateurs.add(personne);
                }
            }
            
            // On renvoie les utilisateurs d??c??d??s
            model.addAttribute("utilisateurs", utilisateurs);
            
            // Si aucun utilisateur est d??c??d??, on envoie un message
            if (utilisateurs.isEmpty()) {
                String resultat = "Aucun utilisateur n'est d??c??d?? ce mois-ci";
                model.addAttribute("resultat", resultat);
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        return "pageAdmin";
    }


    

    // Pistes pour ex??cuter automatiquement les volont??s si 
    // l'utilisateur est d??tect?? dans le scan
    
    
    /*
    public String excuterVolontes(Utilisateur utilisateur) {
        
        List<Volonte> volontes = utilisateur.getVolontesUtilisateur();
        System.out.println("boo");
        for (Volonte volonte : volontes) {
            
            List<SocialConnection> socials = utilisateur.getSocialConnections();
            
            for (SocialConnection social : socials) {
                // si la connexion est avec Twitter
                
                System.out.println(social.getProviderId());
                if (social.getProviderId().equalsIgnoreCase("twitter")) {
                    System.out.println("ici");
                    /*String providerId = social.getProviderId();
                    String providerUserId = social.getProviderUserId();
                    ConnectionKey key = new ConnectionKey(providerId, providerUserId);
                    //ConnectionKey token = social.getAccessToken();
                    Long idTweet = volonte.getIdTweet();
                    String destinataire = volonte.getUsernameDestinataire();
                    String message = volonte.getMessage();
                    
                    //reTweet(idTweet, key);
                    sendDMessage(destinataire, message);
                    
                    
                }
                
            }
            
            
        }
        System.out.println("");
        return "redirect:/admin/pageAdmin";
    }
    
    
    public void reTweet(Long tweetId, ConnectionKey key) {
        Twitter twitter = configureTwitter();
        try {
            twitter.timelineOperations().retweet(tweetId);
        } 
        catch (RuntimeException ex) {
        }

    }
    
    private Twitter configureTwitter() {
        // pb : connectionRepository in null ???
        Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter.class);
        Twitter twitter = connection.getApi();
        return twitter;
    }
    
    // envoyer un message --> ne fonctionne pas, pb d'authorisation
    public void sendDMessage(String destinataire, String message) {
        System.out.println("Salut");
        Twitter twitter = configureTwitter();
        try {
            twitter.directMessageOperations().sendDirectMessage("BaptisteVilled1", "salut c'est moi");
        } catch (Exception ex) {
            
        }
        
    }*/
    
}
