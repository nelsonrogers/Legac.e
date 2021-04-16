/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.security;

/**
 *
 * @author nelsonrogers
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

import testament.entity.Utilisateur;
import testament.util.UserHelper;

@Service
public class SocialConnectionSignup implements ConnectionSignUp {

    @Autowired
    UserHelper userHelper;

    /**
     * This is where you would create the user object and persist it
     */
    @Override
    public String execute(Connection<?> connection) {
        //create the user based on the API type (Facebook or Twiter)
        Utilisateur user = userHelper.getUser(connection);
        
        //return the user name
        return user.getUsername();
    }

}