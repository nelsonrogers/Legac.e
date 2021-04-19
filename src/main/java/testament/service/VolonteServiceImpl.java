/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.service;

import testament.dao.VolonteRepository;
import testament.entity.Volonte;

/**
 *
 * @author nelsonrogers
 */
public class VolonteServiceImpl implements VolonteService {
    
    private final VolonteRepository volonteRepository;

    public VolonteServiceImpl(VolonteRepository volonteRepository) {
        this.volonteRepository = volonteRepository;
    }

    
    @Override
    public void save(Volonte volonte) {
        volonteRepository.save(volonte);        
    }
}
