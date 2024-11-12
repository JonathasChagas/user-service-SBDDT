package academy.devdojo.user_service.service;

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
    private final List<User> usersList = new ArrayList<>();

    @BeforeEach
    void init() {
        var ana = User.builder().id(1L).firstName("Ana").lastName("Oliveira").email("anaoliveira@email.com").build();
        var marcos = User.builder().id(2L).firstName("Marcos").lastName("Ferreira").email("marcosferreira@email.com").build();
        var julia = User.builder().id(3L).firstName("Julia").lastName("Gomes").email("juliagomes@email.com").build();
        var paulo = User.builder().id(4L).firstName("Paulo").lastName("Lima").email("paulolima@email.com").build();
        var camila = User.builder().id(5L).firstName("Camila").lastName("Costa").email("camilacosta@email.com").build();
        var roberto = User.builder().id(6L).firstName("Roberto").lastName("Alves").email("robertoalves@email.com").build();
        usersList.addAll(List.of(ana, marcos, julia, paulo, camila, roberto));
    }

    @Test
    @DisplayName("findAll returns a list with all users when argument is null")
    @Order(1)
    public void findAll_ReturnsAllUsers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(usersList);

        var users = service.findAll(null);

        Assertions.assertThat(users).isNotNull().isNotEmpty().hasSameElementsAs(usersList);
    }

    @Test
    @DisplayName("findAll returns list with found objects when name exists")
    @Order(2)
    public void findAll_ReturnsUserInList_WhenNameIsFound() {
        var user = usersList.getFirst();
        var expectedUserFound = Collections.singletonList(user);

        BDDMockito.when(repository.findByName(user.getFirstName())).thenReturn(expectedUserFound);

        var users = service.findAll(user.getFirstName());
        Assertions.assertThat(users).isNotNull().isNotEmpty().containsExactlyElementsOf(expectedUserFound);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(3)
    public void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        var name = "not found";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        var users = service.findAll(name);

        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns a user with given id")
    @Order(4)
    public void findById_ReturnsUser_WhenIdIsFound() {
        var userToFind = usersList.getFirst();

        BDDMockito.when(repository.findById(userToFind.getId())).thenReturn(Optional.of(userToFind));

        var user = service.findByIdOrThrowNotFound(userToFind.getId());

        Assertions.assertThat(user).isNotNull().isEqualTo(userToFind);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when user is not found")
    @Order(5)
    public void findById_ThrowsResponseStatusException_WhenIdIsNotFound() {
        var userToFind = usersList.getFirst();
        BDDMockito.when(repository.findById(userToFind.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(userToFind.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a user")
    @Order(6)
    public void save_SavesUser_WhenSuccessful() {
        var user = User.builder().id(7L).firstName("José").lastName("Campos").email("josecampos@email.com").build();
        BDDMockito.when(repository.save(user)).thenReturn(user);

        var savedUser = service.save(user);

        Assertions.assertThat(savedUser).isEqualTo(user).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes a user")
    @Order(7)
    public void delete_RemovesUser_WhenSuccessful() {
        var userToDelete = usersList.getFirst();

        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
        BDDMockito.doNothing().when(repository).delete(userToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when user is not found")
    @Order(8)
    public void delete_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var userToDelete = usersList.getFirst();

        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(userToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a user")
    @Order(9)
    public void update_UpdatesUser_WhenSuccessful() {
        var userToUpdate = usersList.getFirst();
        userToUpdate.setEmail("designerana@email.com");

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.doNothing().when(repository).update(userToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when user is not found")
    @Order(10)
    public void update_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var userToUpdate = usersList.getFirst();
        userToUpdate.setEmail("designerana@email.com");

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}