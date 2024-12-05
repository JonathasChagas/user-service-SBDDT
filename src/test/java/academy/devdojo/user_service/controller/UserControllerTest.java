package academy.devdojo.user_service.controller;

import academy.devdojo.exception.NotFoundException;
import academy.devdojo.user_service.commons.FileUtils;
import academy.devdojo.user_service.commons.UserUtils;
import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "academy.devdojo")
class UserControllerTest {
    private static final String URL = "/v1/users";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository repository;
    private List<User> usersList;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        usersList = userUtils.newUserList();
    }

    @Test
    @DisplayName("GET v1/users returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(usersList);
        var response = fileUtils.readResourceFile("user/get-user-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?param=Ana returns a list with found object when name exists")
    @Order(2)
    void findAll_ReturnsUsersInList_WhenNameIsFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-ana-name-200.json");
        var name = "Ana";
        var ana = usersList.stream().filter(user -> user.getFirstName().equals(name)).findFirst().orElse(null);

        BDDMockito.when(repository.findByFirstNameIgnoreCase(name)).thenReturn(Collections.singletonList(ana));

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?param=x returns a list with found object when name exists")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/1 returns a user when id exists")
    @Order(4)
    void findById_ReturnsUserById_WhenSuccessful() throws Exception {
        BDDMockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(usersList.getFirst()));
        var response = fileUtils.readResourceFile("user/get-user-by-id-200.json");
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/99 throws NotFound 404 when user is not found")
    @Order(5)
    void findById_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
        var id = 99L;

        BDDMockito.when(repository.findById(id)).thenThrow(new NotFoundException("User not Found"));

        var response = fileUtils.readResourceFile("user/get-user-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/users creates a user")
    @Order(6)
    void save_CreatesUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-user-200.json");
        var response = fileUtils.readResourceFile("user/post-response-user-201.json");
        var userToSave = userUtils.newSavedUser();

        BDDMockito.when(repository.save(userToSave)).thenReturn(userToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/users updates a user")
    @Order(7)
    void update_UpdateUser_WhenSucccessful() throws Exception {
        BDDMockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(usersList.getFirst()));
        var request = fileUtils.readResourceFile("user/put-request-user-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/users throws NotFound when user is not found")
    @Order(8)
    void update_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
        var request = fileUtils.readResourceFile("user/put-request-user-400.json");
        var response = fileUtils.readResourceFile("user/put-user-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DEL v1/users/1 removes a user")
    @Order(9)
    void delete_RemoveUser_WhenSuccessful() throws Exception {
        BDDMockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(usersList.getFirst()));

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", 1L)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DEL v1/users/99 throws NotFound when user is not found")
    @Order(10)
    void delete_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("user/delete-user-by-id-404.json");
        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users returns bad request when fields are empty")
    @Order(11)
    void save_ReturnsBadRequest_WhenFielsAreEmpty(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/users returns bad request when fields are invalid")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);

    }

    private static Stream<Arguments> postUserBadRequestSource() {
        var allErrors = allRequiredErrors();
        var emailError = invalidEmailErrors();

        return Stream.of(
                Arguments.of("post-request-user-empty-fields-400.json", allErrors),
                Arguments.of("post-request-user-blank-fields-400.json", allErrors),
                Arguments.of("post-request-user-invalid-email-400.json", emailError)
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        var allErrors = allRequiredErrors();
        allErrors.add("The field 'id' cannot be null");
        var emailError = invalidEmailErrors();

        return Stream.of(
                Arguments.of("put-request-user-empty-fields-400.json", allErrors),
                Arguments.of("put-request-user-blank-fields-400.json", allErrors),
                Arguments.of("put-request-user-invalid-email-400.json", emailError)
        );
    }

    private static List<String> invalidEmailErrors() {
        var emailInvalidError = "The e-mail is not valid";
        return new ArrayList<>(List.of(emailInvalidError));
    }

    private static List<String> allRequiredErrors() {
        var firstNameError = "The field 'name' is required";
        var lastNameError = "The field 'second name' is required";
        var emailRequiredError = "The field 'email' is required";
        return new ArrayList<>(List.of(firstNameError, lastNameError, emailRequiredError));
    }
}