package academy.devdojo.user_service.repository;

import academy.devdojo.user_service.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserHardCodedRepositoryTest {
    @InjectMocks
    private UserHardCodedRepository repository;
    @Mock
    private UserData userData;
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
    @DisplayName("findAll retuns all users when successful")
    @Order(1)
    public void findAll_ReturnsUsersInList_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var users = repository.findAll();

        Assertions.assertThat(users).isNotNull().isNotEmpty().hasSameElementsAs(usersList);
    }

    @Test
    @DisplayName("findById returns user when found")
    @Order(2)
    public void findById_ReturnsUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var expectedUser = usersList.getFirst();
        var user = repository.findById(1L);

        Assertions.assertThat(user).isPresent().contains(expectedUser);
    }

    @Test
    @DisplayName("findByName returns a empty list when name is null")
    @Order(3)
    public void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var users = repository.findByName(null);
        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns a list of users when found")
    @Order(4)
    public void findByName_ReturnsProducersInList_WhenNameIsFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var expectedUser = usersList.getFirst();
        var users = repository.findByName(expectedUser.getFirstName());

        Assertions.assertThat(users).isNotNull().isNotEmpty().contains(expectedUser);
    }

    @Test
    @DisplayName("save creates a user")
    @Order(5)
    public void save_SaveProducer_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var userToSave = User.builder().firstName("Maurício").lastName("Gomes").email("mauriciogomes@email.com").build();
        var savedUser = repository.save(userToSave);

        Assertions.assertThat(savedUser).isNotNull().isEqualTo(userToSave);
    }

    @Test
    @DisplayName("delete removes a user")
    @Order(6)
    public void delete_RemoveUroducer_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var userToDelete = usersList.getFirst();
        repository.delete(userToDelete);

        Assertions.assertThat(usersList).isNotEmpty().doesNotContain(userToDelete);
    }

    @Test
    @DisplayName("update updates a user")
    @Order(7)
    public void update_UpdateUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        var userToUpdate = usersList.getFirst();
        userToUpdate.setEmail("designerana@email.com");

        repository.update(userToUpdate);

        Assertions.assertThat(usersList).contains(userToUpdate);

        var userUpdatedOptional = repository.findById(userToUpdate.getId());

        Assertions.assertThat(userUpdatedOptional).isPresent();
        Assertions.assertThat(userUpdatedOptional.get().getEmail()).isEqualTo(userToUpdate.getEmail());
    }
}