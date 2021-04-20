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
    
    
    @PostMapping("/newScan")
    public String scan(Model model, RedirectAttributes redirectInfo) throws IOException {
        
        utilisateurs = new ArrayList<>();
        String resultat;
        
        try (FileReader fileReader = new FileReader("src/main/resources/static/deces-2021-m02.txt"); 
            BufferedReader reader = new BufferedReader(fileReader)) {
            String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));

            System.out.println(data);
            for (Utilisateur personne : dao.findAll()) {
            
                String date = personne.getDateNaiss().toString();
                String dateSimple = date.replace("-", "");
                              
                // La personne a forcément un prénom
                String ligne = personne.getNom().toUpperCase() + "*" + personne.getPrenom().toUpperCase();

                // si deuxième prénom
                if (personne.getPrenom2() != null && !personne.getPrenom2().equals("")) {
                    ligne += " " + personne.getPrenom2().toUpperCase();
                }
                //si troisième prénom
                if (personne.getPrenom3() != null && !personne.getPrenom3().equals("")) {
                    ligne += " " + personne.getPrenom3().toUpperCase();
                }

                // fin des prénoms
                ligne += "/";


                int longueur = ligne.length();

                // on complète par des espaces jusqu'à la position 81
                int nbEspaces = 80-longueur;
                for (int i=0 ; i < nbEspaces ; i++) {
                    ligne += " ";
                }

                // sexe : 1 ou 2, puis date de naissance, code commune, nom commune tous enchainés
                ligne += personne.getSexe() + dateSimple + personne.getCodePostal() + personne.getCommuneNaiss().toUpperCase();


                System.out.println(ligne);
                
                // si un utilisateur est décédé, on l'ajoute à la liste 
                if(data.contains(ligne)) {
                    utilisateurs.add(personne);
                }
            }
            
            model.addAttribute("utilisateurs", utilisateurs);
            
            
            if (utilisateurs.isEmpty()) {
                resultat = "Aucun utilisateur n'est décédé ce mois-ci";
                redirectInfo.addFlashAttribute("resultat", resultat);
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return "pageAdmin";
    }

    
    
    
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
            tweetId = 1380430292486328324L; // ici on définit pour simplifier la démonstration
            twitter.timelineOperations().retweet(tweetId);
        } 
        catch (RuntimeException ex) {
        }

    }
    
    private Twitter configureTwitter() {
        
        Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter.class);
        Twitter twitter = connection.getApi();
        return twitter;
    }
    
    public void sendDMessage(String destinataire, String message) {
        System.out.println("Salut");
        Twitter twitter = configureTwitter();
        try {
            twitter.directMessageOperations().sendDirectMessage("BaptisteVilled1", "salut c'est moi");
        } catch (Exception ex) {
            
        }
        
    }*/
    
}
