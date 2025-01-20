package academy.devdojo.user_service.service;

import academy.devdojo.exception.EmailExistsException;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.user_service.commons.UserUtils;
import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository repository;
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

        BDDMockito.when(repository.findByFirstNameIgnoreCase(user.getFirstName())).thenReturn(expectedUserFound);

        var users = service.findAll(user.getFirstName());
        Assertions.assertThat(users).isNotNull().isNotEmpty().containsExactlyElementsOf(expectedUserFound);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        var name = "not found";
        BDDMockito.when(repository.findByFirstNameIgnoreCase(name)).thenReturn(Collections.emptyList());

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
    @DisplayName("findById throws NotFoundException when user is not found")
    @Order(5)
    void findById_ThrowsNotFoundException_WhenIdIsNotFound() {
        var userToFind = usersList.getFirst();
        BDDMockito.when(repository.findById(userToFind.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(userToFind.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("save creates a user")
    @Order(6)
    void save_SavesUser_WhenSuccessful() {
        var userToSave = userUtils.newSavedUser();

        BDDMockito.when(repository.save(userToSave)).thenReturn(userToSave);
        BDDMockito.when(repository.findByEmail(userToSave.getEmail())).thenReturn(Optional.empty());

        var savedUser = service.save(userToSave);

        Assertions.assertThat(savedUser).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("save throws EmailExistsException when email already exists")
    @Order(7)
    void save_ThrowsEmailExistsException_WhenEmailAlreadyExists() {
        var savedUser = usersList.getLast();
        var userToSave = userUtils.newSavedUser().withEmail(savedUser.getEmail());
        var email = userToSave.getEmail();

        BDDMockito.when(repository.findByEmail(email)).thenReturn(Optional.of(savedUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.save(userToSave))
                .isInstanceOf(EmailExistsException.class);
    }

    @Test
    @DisplayName("delete removes a user")
    @Order(8)
    void delete_RemovesUser_WhenSuccessful() {
        var userToDelete = usersList.getFirst();

        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
        BDDMockito.doNothing().when(repository).delete(userToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws NotFoundException when user is not found")
    @Order(9)
    void delete_ThrowsNotFoundException_WhenUserIsNotFound() {
        var userToDelete = usersList.getFirst();

        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(userToDelete.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("update updates a user")
    @Order(10)
    void update_UpdatesUser_WhenSuccessful() {
        var userToUpdate = usersList.getFirst().withEmail(userUtils.newEmailUser());
        var email = userToUpdate.getEmail();
        var id = userToUpdate.getId();

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(email, id)).thenReturn(Optional.empty());
        BDDMockito.when(repository.save(userToUpdate)).thenReturn(userToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @DisplayName("update throws NotFoundException when user is not found")
    @Order(11)
    void update_ThrowsNotFoundException_WhenUserIsNotFound() {
        var userToUpdate = usersList.getFirst().withEmail(userUtils.newEmailUser());

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("update throws EmailExistsException when email belongs to another user")
    @Order(12)
    void update_ThrowsEmailExistsException_WhenEmailBelongsToAnotherUser() {
        var savedUser = usersList.getLast();
        var userToUpdate = usersList.getFirst();

        var email = userToUpdate.getEmail();
        var id = userToUpdate.getId();

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(email, id)).thenReturn(Optional.of(savedUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(EmailExistsException.class);
    }
}