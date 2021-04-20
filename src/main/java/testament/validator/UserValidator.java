package testament.validator;

import testament.entity.Utilisateur;
import testament.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Utilisateur.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Utilisateur user = (Utilisateur) o;

        // si le nom d'utilisateur choisi existe déjà : erreur
        if (userService.findByUserName(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }
        // si mdp invalide
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }
        // si mdp et mdp de confirmation sont différents
        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}