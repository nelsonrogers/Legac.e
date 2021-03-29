package testament.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import Testament.entity.Personne;

// This will be AUTO IMPLEMENTED by Spring 

public interface PersonneRepository extends JpaRepository<Personne, Integer> {

}
