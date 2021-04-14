package testament.entity;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.*;
import lombok.*;

// Un exemple d'entité
// On utilise Lombok pour auto-générer getter / setter / toString...
// cf. https://examples.javacodegeeks.com/spring-boot-with-lombok/
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
@Entity // Une entité JPA
public class Personne {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id;

    @Column(unique=true)
    @NonNull
    private String nom;
    
    @Column(unique=true)
    @NonNull
    private String adresse;
    
    /*
    @OneToMany(mappedBy = "personne")
    private List<Service> services = new LinkedList<>();
    
    @OneToMany(mappedBy = "personnes")
    private List<Utilisateur> utilisateurs = new LinkedList<>();
    
    @OneToOne
    private Infos_Deces deces;
    */
    
    /*public boolean isDead(){
        
    }*/
}
