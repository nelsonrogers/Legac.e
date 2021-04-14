/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.entity;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author bapti
 */
public enum Reseau {
    FACEBOOK,TWITTER,INSTAGRAM,PINTEREST,PAYPAL,GOOGLE;
    
    /*
    @OneToOne(mappedBy="reseauutilisateur")
    private Utilisateur usersreseau;
    
    @OneToMany(mappedBy="reseau")
    private Service servicereseau;
    */
}
