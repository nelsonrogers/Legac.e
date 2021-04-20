/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import testament.entity.Utilisateur;
import testament.entity.Volonte;

/**
 *
 * @author nelsonrogers
 */
public interface VolonteRepository extends JpaRepository<Volonte, Long>{
    
}
