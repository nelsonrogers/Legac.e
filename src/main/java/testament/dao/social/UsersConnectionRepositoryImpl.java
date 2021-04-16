/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.dao.social;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import testament.dao.SocialConnectionRepository;
import testament.entity.SocialConnection;

public class UsersConnectionRepositoryImpl implements UsersConnectionRepository{
    @Autowired 
    SocialConnectionRepository dao;

    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        List<String> result =new LinkedList<>();
        for (SocialConnection socialConnection: dao.findAll()) {
            if (connection.getKey().getProviderId().equals(socialConnection.getProviderId()) &&
                connection.getKey().getProviderUserId().equals(socialConnection.getProviderUserId()))
                result.add(socialConnection.getOwner().getUsername());
        }
        return result;
    }


    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        Set<String> result = new HashSet<>();
        for (SocialConnection socialConnection: dao.findAll()) {
            if (providerId.equals(socialConnection.getProviderId()) &&
                providerUserIds.contains(socialConnection.getProviderUserId()))
                result.add(socialConnection.getOwner().getUsername());
        }
        return result;
    }

    public ConnectionRepository createConnectionRepository(String userId) {
        return new ConnectionRepositoryImpl(userId);
    }
    
}