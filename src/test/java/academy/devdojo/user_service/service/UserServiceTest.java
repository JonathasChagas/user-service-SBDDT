package academy.devdojo.user_service.service;

import academy.devdojo.user_service.commons.UserUtils;
import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.repository.UserHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;
    @Mock
    private UserHardCodedRepository repository;
    private List<User> usersList;
    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        usersList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(usersList);

        var users = service.findAll(null);

        Assertions.assertThat(users).isNotNull().isNotEmpty().hasSameElementsAs(usersList);
    }

    @Test
    @DisplayName("findAll returns list with found objects when name exists")
    @Order(2)
    void findAll_ReturnsUserInList_WhenNameIsFound() {
        var user = usersList.getFirst();
        var expectedUserFound = Collections.singletonList(user);

        BDDMockito.when(repository.findByName(user.getFirstName())).thenReturn(expectedUserFound);

        var users = service.findAll(user.getFirstName());
        Assertions.assertThat(users).isNotNull().isNotEmpty().containsExactlyElementsOf(expectedUserFound);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        var name = "not found";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        var users = service.findAll(name);

        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns a user with given id")
    @Order(4)
    void findById_ReturnsUser_WhenIdIsFound() {
        var userToFind = usersList.getFirst();

        BDDMockito.when(repository.findById(userToFind.getId())).thenReturn(Optional.of(userToFind));

        var user = service.findByIdOrThrowNotFound(userToFind.getId());

        Assertions.assertThat(user).isNotNull().isEqualTo(userToFind);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when user is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenIdIsNotFound() {
        var userToFind = usersList.getFirst();
        BDDMockito.when(repository.findById(userToFind.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(userToFind.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a user")
    @Order(6)
    void save_SavesUser_WhenSuccessful() {
        var user = userUtils.newSavedUser();
        BDDMockito.when(repository.save(user)).thenReturn(user);

        var savedUser = service.save(user);

        Assertions.assertThat(savedUser).isEqualTo(user).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes a user")
    @Order(7)
    void delete_RemovesUser_WhenSuccessful() {
        var userToDelete = usersList.getFirst();

        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
        BDDMockito.doNothing().when(repository).delete(userToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when user is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var userToDelete = usersList.getFirst();

        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(userToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a user")
    @Order(9)
    void update_UpdatesUser_WhenSuccessful() {
        var userToUpdate = usersList.getFirst();
        userToUpdate.setEmail(userUtils.newEmailUser());

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.doNothing().when(repository).update(userToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when user is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var userToUpdate = usersList.getFirst();
        userToUpdate.setEmail(userUtils.newEmailUser());

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}