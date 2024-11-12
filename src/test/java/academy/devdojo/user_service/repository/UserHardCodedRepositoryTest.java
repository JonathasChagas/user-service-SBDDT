package academy.devdojo.user_service.repository;

import academy.devdojo.user_service.commons.UserUtils;
import academy.devdojo.user_service.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserHardCodedRepositoryTest {
    @InjectMocks
    private UserHardCodedRepository repository;
    @Mock
    private UserData userData;
    private List<User> usersList;
    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        usersList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll retuns all users when successful")
    @Order(1)
    void findAll_ReturnsUsersInList_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var users = repository.findAll();

        Assertions.assertThat(users).isNotNull().isNotEmpty().hasSameElementsAs(usersList);
    }

    @Test
    @DisplayName("findById returns user when found")
    @Order(2)
    void findById_ReturnsUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var expectedUser = usersList.getFirst();
        var user = repository.findById(1L);

        Assertions.assertThat(user).isPresent().contains(expectedUser);
    }

    @Test
    @DisplayName("findByName returns a empty list when name is null")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var users = repository.findByName(null);
        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns a list of users when found")
    @Order(4)
    void findByName_ReturnsUsersInList_WhenNameIsFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var expectedUser = usersList.getFirst();
        var users = repository.findByName(expectedUser.getFirstName());

        Assertions.assertThat(users).isNotNull().isNotEmpty().contains(expectedUser);
    }

    @Test
    @DisplayName("save creates a user")
    @Order(5)
    void save_SaveUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var userToSave = userUtils.newSavedUser();
        var savedUser = repository.save(userToSave);

        Assertions.assertThat(savedUser).isNotNull().isEqualTo(userToSave).hasNoNullFieldsOrProperties();

        var producerSavedOptional = repository.findById(savedUser.getId());
        Assertions.assertThat(producerSavedOptional).isPresent().contains(userToSave);
    }

    @Test
    @DisplayName("delete removes a user")
    @Order(6)
    void delete_RemoveUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var userToDelete = usersList.getFirst();
        repository.delete(userToDelete);

        Assertions.assertThat(usersList).isNotEmpty().doesNotContain(userToDelete);
    }

    @Test
    @DisplayName("update updates a user")
    @Order(7)
    void update_UpdateUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var userToUpdate = usersList.getFirst();
        userToUpdate.setEmail(userUtils.newEmailUser());

        repository.update(userToUpdate);

        Assertions.assertThat(usersList).contains(userToUpdate);

        var userUpdatedOptional = repository.findById(userToUpdate.getId());

        Assertions.assertThat(userUpdatedOptional).isPresent();
        Assertions.assertThat(userUpdatedOptional.get().getEmail()).isEqualTo(userToUpdate.getEmail());
    }
}