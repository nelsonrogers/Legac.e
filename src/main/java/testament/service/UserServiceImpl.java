package testament.service;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import testament.dao.RoleRepository;
import testament.dao.UserRepository;
import testament.entity.Role;
import testament.entity.Utilisateur;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import testament.entity.Personne;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    // Login et Password de l'administrateur son définis dans 'application.properties'
    @Value("${admin.login}")
    private String adminLogin;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${admin.email}")
    private String adminEmail;
    /*@Value("${}admin.telephone")
    private String adminTelephone;
    @Value("${}admin.nom")
    private String adminNom;
    @Value("${}admin.prenom")
    private String adminPrenom;
    @Value("${}admin.prenom2")
    private String adminPrenom2;
    @Value("${}admin.prenom3")
    private String adminPrenom3;
    @Value("${}admin.sexe")
    private String adminSexe; 
    @Value("${}admin.code_postal")
    private String adminCodePostal; 
    @Value("${}admin.commune_naiss")
    private String adminCommuneNaiss; 
    @Value("${}admin.proche")
    private String adminProche;
    @Value("${}admin.email_proche")
    private String adminEmailProche;
    @Value("${}admin.proche2")
    private String adminProche2;
    @Value("${}admin.email_proche2")
    private String adminEmailProche2;
    @Value("${}admin.date_naiss")
    private LocalDate adminDateNaiss;*/
    

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void save(Utilisateur user) {
        // Par défaut on attribue le rôle 'ROLE_USER' aux nouveaux utilisateurs
        // Ce rôle est créé automatiquement au lancement de l'application
        Role normal = roleRepository.findByName("ROLE_USER").orElseThrow();
        // On crypte le mot de passe avant de l'enregistrer
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.getRoles().add(normal);
        userRepository.save(user);
    }

    @Override
    public Utilisateur findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void initializeRolesAndAdmin() {
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            log.info("Création des deux rôles et de l'administrateur");
            Role roleAdmin = new Role("ROLE_ADMIN");
            Role roleUser = new Role("ROLE_USER");
            Role roleFamille = new Role("ROLE_FAMILLE");
            roleRepository.save(roleAdmin);
            roleRepository.save(roleUser);
            roleRepository.save(roleFamille);
            Utilisateur firstAdmin = new Utilisateur(adminLogin, adminPassword, adminEmail); //, adminTelephone, adminNom, adminPrenom, adminDateNaiss, adminSexe, adminCodePostal, adminCommuneNaiss, adminProche, adminEmailProche);
            // On crypte le mot de passe avant de l'enregistrer
            firstAdmin.setPassword(bCryptPasswordEncoder.encode(firstAdmin.getPassword()));
            firstAdmin.getRoles().add(roleAdmin);
            userRepository.save(firstAdmin);
        }
    }
}
