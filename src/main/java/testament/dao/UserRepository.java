package testament.dao;

import testament.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Utilisateur, Integer> {
    Utilisateur findByUsername(String name);
}