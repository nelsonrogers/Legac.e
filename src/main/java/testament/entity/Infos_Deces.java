/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.entity;

import javax.persistence.OneToOne;

/**
 *
 * @author bapti
 */
public class Infos_Deces {
    @OneToOne(mappedBy="deces")
    private Personne personnedecedee;
}
