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
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import testament.util.ConnectionHelper;
import testament.util.ConnectionType;

@Service
public class SocialSignInAdapter implements SignInAdapter {
    
    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        //get the authentication token
        Authentication authentication = getAuthentication(localUserId, connection);
        
        //put the authentication token in the context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        //return the "success" page
        return "/socialloginsuccess";
    }
    
    
    /**
     * Creates the token for an authentication request
     * 
     * @param localUserId
     * @param connection
     * @return token for an authentication request
     */
    private Authentication getAuthentication(String localUserId, Connection<?> connection) {
        //get the roles
        List<GrantedAuthority> roles = getRoles(connection);
       
        //no need for password here because the user logged in with social media
        String password = null;
        
        //instantiate the authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(localUserId, password, roles);
        
        //get out
        return authentication;
    }
    
    
    /**
     * Returns a List of roles. For this demo, there's only one role.
     * The role is defined by how the user logged in (Facebook or Twitter)
     * 
     * @param connection
     * @return list of roles
     */
    private List<GrantedAuthority> getRoles(Connection<?> connection) {
        //start with an empty list
        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        
        //get the connection type
        ConnectionType type = ConnectionHelper.getConnectionType(connection);
        
        //set the role based on the type
        String role = type.toString();
        
        //add the role to the list
        roles.add(new SimpleGrantedAuthority(role));
        
        //get out
        return roles;
    }
}