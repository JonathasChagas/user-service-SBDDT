package academy.devdojo.user_service.repository;

import academy.devdojo.user_service.domain.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class UserData {
    private final List<User> users = new ArrayList<>();

    {
        var william = User.builder().id(1L).firstName("William").lastName("Suane").email("williamsuane@email.com").build();
        var wildnei = User.builder().id(2L).firstName("Wildnei").lastName("Suane").email("wildneisuane@email.com").build();
        var brenon = User.builder().id(3L).firstName("Brenon").lastName("Araujo").email("brenonaraujo@email.com").build();
        var sandra = User.builder().id(4L).firstName("Sandra").lastName("Suane").email("sandrasuane@email.com").build();
        var david = User.builder().id(5L).firstName("David").lastName("Silva").email("davidsilva@email.com").build();
        var fabricio = User.builder().id(6L).firstName("Fabricio").lastName("Chousa").email("fabriciochousa@email.com").build();
        users.addAll(List.of(william, wildnei, brenon, sandra, david, fabricio));
    }
}
