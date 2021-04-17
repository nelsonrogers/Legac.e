package testament.entity;

import java.time.LocalDate;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
@Entity 
public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @NonNull // Lombok
    // Contraintes de taille
    @Size(min = 6, max = 32)
    private String username;
    
    @NonNull // Lombok
    private String password;
    
    @NonNull // Lombok
    @Email // Doit avoir la forme d'une adresse email
    private String email;
    
    @NonNull
    private String telephone;
    
    @NonNull
    private String nom;
    
    @NonNull
    private String prenom;
    
    @Nullable
    private String prenom2;
    
    @Nullable
    private String prenom3;
    
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateNaiss;
    
    @NonNull
    private String sexe;
    
    @NonNull
    private String codePostal;
    
    @NonNull
    private String communeNaiss;
    
    @Nullable
    private String proche;
    
    @Nullable
    @Email
    private String emailProche;
    
    @Nullable
    private String proche2;
    
    @Nullable
    @Email
    private String emailProche2;

    private boolean testament;
   

    @Transient // Non enregistré dans la BD
    private String passwordConfirm;

    @ManyToMany(fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE)
    private List<Role> roles = new LinkedList<>();
    

    @OneToMany(mappedBy="owner", fetch = FetchType.LAZY)
    @Setter(AccessLevel.NONE)    
    private List<SocialConnection> socialConnections = new LinkedList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}