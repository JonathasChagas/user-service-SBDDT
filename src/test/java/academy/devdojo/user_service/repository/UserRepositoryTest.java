package academy.devdojo.user_service.repository;

import academy.devdojo.user_service.commons.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserUtils.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;
    @Autowired
    private UserUtils userUtils;

    @Test
    @DisplayName("save creates a user")
    @Order(1)
    void save_SavesUser_WhenSuccessful() {
        var userToSave = userUtils.newSavedUser();
        var savedUser = repository.save(userToSave);

        Assertions.assertThat(savedUser).hasNoNullFieldsOrProperties();
        Assertions.assertThat(savedUser.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findAll returns a list with all users")
    @Order(2)
    @Sql("/sql/init_one_user.sql")
    void findAll_ReturnsAllUsers_WhenSuccessful() {
        var users = repository.findAll();
        Assertions.assertThat(users).isNotEmpty();
    }
}