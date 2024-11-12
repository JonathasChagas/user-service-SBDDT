package academy.devdojo.user_service.commons;

import academy.devdojo.user_service.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {

    public List<User> newUserList() {
        var ana = User.builder().id(1L).firstName("Ana").lastName("Oliveira").email("anaoliveira@email.com").build();
        var marcos = User.builder().id(2L).firstName("Marcos").lastName("Ferreira").email("marcosferreira@email.com").build();
        var julia = User.builder().id(3L).firstName("Julia").lastName("Gomes").email("juliagomes@email.com").build();
        var paulo = User.builder().id(4L).firstName("Paulo").lastName("Lima").email("paulolima@email.com").build();
        var camila = User.builder().id(5L).firstName("Camila").lastName("Costa").email("camilacosta@email.com").build();
        var roberto = User.builder().id(6L).firstName("Roberto").lastName("Alves").email("robertoalves@email.com").build();
        return new ArrayList<>(List.of(ana, marcos, julia, paulo, camila, roberto));
    }

    public User newSavedUser() {
        return User.builder().id(7L).firstName("José").lastName("Campos").email("josecampos@email.com").build();
    }

    public String newEmailUser() {
        return "designerana@email.com";
    }
}
