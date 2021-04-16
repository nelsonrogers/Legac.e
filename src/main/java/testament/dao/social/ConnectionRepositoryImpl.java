/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.dao.social;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.extern.slf4j.Slf4j;
import testament.dao.SocialConnectionRepository;
import testament.dao.UserRepository;
import testament.entity.SocialConnection;
import testament.entity.Utilisateur;

@Slf4j
public class ConnectionRepositoryImpl implements ConnectionRepository {
    @Autowired
    UserRepository utilisateurDao;

    @Autowired
    SocialConnectionRepository socialConnectionDao;


    @Autowired 
    ConnectionFactoryLocator connectionFactoryLocator;
    
    final String userId;
    public ConnectionRepositoryImpl(String userId) {
        log.info("ConnectionRepository created for userId '{}'", userId);
        this.userId = userId;
    }

    private Connection<?> constructConnection(SocialConnection socialConnection, ConnectionFactory<?> factory ) {
        ConnectionData data = new ConnectionData(
                socialConnection.getProviderId(),
                socialConnection.getProviderUserId(),
                socialConnection.getDisplayName(),
                socialConnection.getProfileUrl(),
                socialConnection.getImageUrl(),
                socialConnection.getAccessToken(),
                socialConnection.getSecret(),
                socialConnection.getRefreshToken(),
                socialConnection.getExpireTime()
        );
        return factory.createConnection(data);

    }

    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        log.info("Finding all connections for userId '{}'", userId);

        MultiValueMap<String, Connection<?>> result = new LinkedMultiValueMap<>();

		Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
		for (String registeredProviderId : registeredProviderIds) {
			result.put(registeredProviderId, Collections.<Connection<?>>emptyList());
		}

        Utilisateur utilisateur = utilisateurDao.findByUsername(userId);
        utilisateur.getSocialConnections().forEach( socialConnection -> {
            String providerId = socialConnection.getProviderId();
            if (result.get(providerId).isEmpty()) {
				result.put(providerId, new LinkedList<>());
			}
            ConnectionFactory<?> factory = connectionFactoryLocator.getConnectionFactory(socialConnection.getProviderId());
            result.add(socialConnection.getProviderId(), constructConnection(socialConnection, factory));
        });

        return result;
    }

    public List<Connection<?>> findConnections(String providerId) {
        log.info("Finding connections for providerId '{}' and userId '{}'", providerId, userId);

        List<Connection<?>> result = new LinkedList<>();

        Utilisateur utilisateur = utilisateurDao.findByUsername(userId);
        utilisateur.getSocialConnections().forEach( socialConnection -> {
            if (socialConnection.getProviderId().equals(providerId)) {
                ConnectionFactory<?> factory = connectionFactoryLocator.getConnectionFactory(providerId);
                result.add(constructConnection(socialConnection, factory));
            }
        });

        return result;
    }

    @SuppressWarnings("unchecked")
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        List<?> result = findConnections(getProviderId(apiType));
        return (List<Connection<A>>) result;       
    }

    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUserIds) {
        log.info("Finding connections to users for  userId '{}'", userId);
        if (providerUserIds == null || providerUserIds.isEmpty()) {
			throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
		}
        MultiValueMap<String, Connection<?>> result = new LinkedMultiValueMap<>();

		Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
		for (String registeredProviderId : registeredProviderIds) {
			result.put(registeredProviderId, new LinkedList<>());
		}   
        Utilisateur utilisateur = utilisateurDao.findByUsername(userId);
        for (String providerId : providerUserIds.keySet()) {
            utilisateur.getSocialConnections().forEach( socialConnection -> {
                ConnectionFactory<?> factory = connectionFactoryLocator.getConnectionFactory(providerId);                
                String providerUserId = socialConnection.getProviderUserId();
                if (providerId.equals(socialConnection.getProviderId()) && 
                    providerUserIds.get(providerId).contains(providerUserId)) {
                        result.add(providerId, constructConnection(socialConnection, factory));    
                    }
            });
        }
        return result;
    }

    public Connection<?> getConnection(ConnectionKey connectionKey) {
        log.info("Finding connections by key {} for userId '{}'", connectionKey, userId);

        Utilisateur utilisateur = utilisateurDao.findByUsername(userId);
        for (SocialConnection socialConnection : utilisateur.getSocialConnections()) {
            if (socialConnection.getProviderId().equals(connectionKey.getProviderId()) &&
                socialConnection.getProviderUserId().equals(connectionKey.getProviderUserId())) {
                ConnectionFactory<?> factory = connectionFactoryLocator.getConnectionFactory(socialConnection.getProviderId());
                return constructConnection(socialConnection, factory);
            }
        }
        throw new NoSuchConnectionException(connectionKey);
    }

    @SuppressWarnings("unchecked")
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
		String providerId = getProviderId(apiType);
		return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
    }

	@SuppressWarnings("unchecked")
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
		String providerId = getProviderId(apiType);
        log.info("Finding primary connection for providerId '{}' and userId '{}'", providerId, userId);
		Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
		if (connection == null) {
			throw new NotConnectedException(providerId);
		}
		return connection;
    }

	@SuppressWarnings("unchecked")
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
		return (Connection<A>) findPrimaryConnection(providerId);
    }

    @Override
    public void addConnection(Connection<?> connection) {
        Utilisateur utilisateur = utilisateurDao.findByUsername(userId);        
		try {
			ConnectionData data = connection.createData();
            log.info("Add connection for providerId '{}' and userId '{}'", data.getProviderId(), userId);
			int rank = 1; // TODO calculer
            SocialConnection nouvelle = new SocialConnection(
                null,
                data.getProviderId(),
                data.getProviderUserId(),
                rank,
                data.getDisplayName(),
                data.getProfileUrl(),
                data.getImageUrl(),
                data.getAccessToken(),
                data.getSecret(),
                data.getRefreshToken(),
                data.getExpireTime(),
                utilisateur
            );
            //utilisateur.getSocialConnections().add(nouvelle);
            //utilisateurDao.save(utilisateur);
            socialConnectionDao.save(nouvelle);
        } catch (DuplicateKeyException e) {
			throw new DuplicateConnectionException(connection.getKey());
		}
        
    }

    public void updateConnection(Connection<?> connection) {
        // TODO Auto-generated method stub      
    }

    public void removeConnections(String providerId) {
        // TODO Auto-generated method stub
    }

    @Override
    public void removeConnection(ConnectionKey connectionKey) {
        // TODO Auto-generated method stub      
    }

	private Connection<?> findPrimaryConnection(String providerId) {
        List<Connection<?>> connections = findConnections(providerId);
        if (connections.isEmpty()) return null;
        return connections.get(0);

    }    

    private <A> String getProviderId(Class<A> apiType) {
		return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
	}    
}
