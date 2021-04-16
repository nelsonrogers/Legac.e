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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import testament.dao.UserRepository;
import testament.entity.Utilisateur;

/**
 *
 * @author nelsonrogers
 */
@Controller
@RequestMapping("/scan")
public class ScannerController {
    

    @Autowired
    private UserRepository dao;
    
    private ArrayList<Utilisateur> utilisateurs; 
    
    @GetMapping("/new")
    public String check() {
        return "pageAdmin";
    }
    
    
    @PostMapping("/new")
    public String scan(Model model) throws IOException {
        utilisateurs = new ArrayList<>();
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

                
                ligne += personne.getSexe() + dateSimple + personne.getCodePostal() + personne.getCommuneNaiss().toUpperCase();


                System.out.println(ligne);

                if(data.contains(ligne)) {
                    utilisateurs.add(personne);
                }
            }
            model.addAttribute("utilisateurs", utilisateurs);
            System.out.println(utilisateurs.toString());
            
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return "pageAdmin";
    }
}
