/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import testament.dao.UserRepository;
import testament.entity.Utilisateur;

/**
 *
 * @author nelsonrogers
 */
public class ScannerController {
    

    @Autowired
    private UserRepository dao;
    
    private ArrayList<Utilisateur> utilisateurs; 
    
    
    @GetMapping("/scan")
    public ArrayList<Utilisateur> scan() {
        utilisateurs = new ArrayList<>();
        
        try {
            File fichierDeces = new File("/Users/nelsonrogers/Desktop/deces-2021-m02.txt");
            Scanner myReader = new Scanner(fichierDeces);
            String data = "";
            while (myReader.hasNextLine()) {
                data += myReader.nextLine();
            }
            
            for (Utilisateur personne : dao.findAll()) {
            
                
                System.out.println(personne.getNom());
                System.out.println(personne.getPrenom());
                System.out.println(personne.getPrenom2());
                System.out.println(personne.getPrenom3());
                System.out.println(personne.getSexe());
                System.out.println(personne.getCodePostal());
                System.out.println(personne.getCommuneNaiss());
                
                
                // La personne a forcément un prénom
                String ligne = personne.getNom().toUpperCase() + "*" + personne.getPrenom().toUpperCase();

                // si deuxième prénom
                if (personne.getPrenom2() != null) {
                    ligne += " " + personne.getPrenom2().toUpperCase();
                }
                //si troisième prénom
                if (personne.getPrenom3() != null) {
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


                ligne += personne.getSexe() + personne.getDateNaiss().toString() + personne.getCodePostal() + personne.getCommuneNaiss().toUpperCase();


                System.out.println(ligne);

                if(data.contains(ligne)) {
                    utilisateurs.add(personne);
                }
        
            }
            
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return utilisateurs;
    }
}
