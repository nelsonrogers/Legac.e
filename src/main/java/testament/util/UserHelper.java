/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.util;

/**
 *
 * @author nelsonrogers
 */
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import testament.entity.Utilisateur;

@Component
public class UserHelper {
    
    /**
     * Instantiates a User object based on login
     * 
     * @param connection
     * @return user object
     */
    public Utilisateur getUser(Connection<?> connection) {
        Utilisateur user = null;

        //get the connection type
        ConnectionType type = ConnectionHelper.getConnectionType(connection);
        
        //create a user based on API type
        if (type.equals(ConnectionType.TWITTER)) {
            user = getTwitterUser(connection);
        } else if (type.equals(ConnectionType.FACEBOOK)) {
            user = getFacebookUser(connection);
        }
        
        return user;
    }
    
    
    /**
     * Handles users who've logged in via Twitter
     */
    private Utilisateur getTwitterUser(Connection<?> connection) {
        Utilisateur user = new Utilisateur();
        Twitter twitterApi = (Twitter)connection.getApi();
        
        String name = twitterApi.userOperations().getUserProfile().getName();
        
        user.setUsername(name);
        
        return user;
    }
    
    
    /**
     * Handles users who've logged in via Facebook
     */
    private Utilisateur getFacebookUser(Connection<?> connection) {
        Utilisateur user = new Utilisateur();
        Facebook facebookApi = (Facebook)connection.getApi();
        
        String name = facebookApi.userOperations().getUserProfile().getName();
        
        user.setUsername(name);
        
        return user;
    }
}
