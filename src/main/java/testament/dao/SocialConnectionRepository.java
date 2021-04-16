package testament.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import testament.entity.SocialConnection;

public interface SocialConnectionRepository extends JpaRepository<SocialConnection, Long> {
    List<SocialConnection> findByProviderId(String providerId);
}
