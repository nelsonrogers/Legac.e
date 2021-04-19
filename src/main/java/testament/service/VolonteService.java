/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.service;

import testament.entity.Utilisateur;
import testament.entity.Volonte;

/**
 *
 * @author nelsonrogers
 */
public interface VolonteService {
    
    void save(Volonte volonte);
    
    //Volonte findByUtilisateur(Utilisateur utilisateur);
}
