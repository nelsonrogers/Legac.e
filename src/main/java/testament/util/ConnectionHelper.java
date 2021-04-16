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

public class ConnectionHelper {

    /**
     * Determines the connection type based on the Connection object;
     * 
     * @param connection
     * @return connection type
     */
    public static ConnectionType getConnectionType(Connection<?> connection) {
        Object api = connection.getApi();
        
        if (api instanceof Twitter) {
            return ConnectionType.TWITTER;
        } else if (api instanceof Facebook) {
            return ConnectionType.FACEBOOK;
        } else {
            throw new RuntimeException("Unknown API!");
        }
    }
}
