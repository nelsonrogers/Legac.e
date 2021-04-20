/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.social.connect.ConnectionKey;
import testament.dao.social.UsersConnectionRepositoryImpl;

@Slf4j
@Configuration
public class SocialConfig {
    
    /* voir les détails de l'implémentation sur le Spring Social Reference Manual :
    https://docs.spring.io/spring-social-twitter/docs/1.0.5.RELEASE/reference/htmlsingle/
    */
    
    @Value("${spring.social.facebook.appId}")
    String clientId;
    @Value("${spring.social.facebook.appSecret}")
    String clientSecret;
    @Value("${spring.social.twitter.consumerKey}")
    String consumerKey;
    @Value("${spring.social.twitter.consumerSecret}")
    String consumerSecret;    


    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        
        registry.addConnectionFactory(new FacebookConnectionFactory(clientId, clientSecret));        
        registry.addConnectionFactory(new TwitterConnectionFactory(consumerKey, consumerSecret));
            
        return registry;
    }

    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
        }
        return usersConnectionRepository().createConnectionRepository(authentication.getName());
    }


    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
        return new UsersConnectionRepositoryImpl();
    }

    @Bean
    public ConnectController connectController() {
        ConnectController controller =  new ConnectController(connectionFactoryLocator(), connectionRepository());
        //controller.setApplicationUrl(applicationUrl);
        return controller;
    }

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }
    
/*        
    @Configuration
    @Profile("dev")
    static class Dev {

        @Bean
        public TextEncryptor textEncryptor() {
            return Encryptors.noOpText();
        }

    }

    @Configuration
    @Profile("prod")
    static class Prod {
        @Value("${security.encryptPassword}")
        String encryptPassword;
        @Value("${security.encryptSalt}")
        String encryptSalt;    
        @Bean
        public TextEncryptor textEncryptor() {
            return Encryptors.text(encryptPassword, encryptSalt);
        }
    }
*/
}
