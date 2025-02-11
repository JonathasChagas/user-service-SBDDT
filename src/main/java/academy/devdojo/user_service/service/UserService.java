package academy.devdojo.user_service.service;

import academy.devdojo.exception.EmailExistsException;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(name);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not Found"));
    }


    public User save(User userToSave) {
        assertEmailDoesNotExist(userToSave.getEmail());
        return repository.save(userToSave);
    }

    public void delete(Long id) {
        var userToDelete = findByIdOrThrowNotFound(id);
        repository.delete(userToDelete);
    }

    public void update(User user) {
        assertUserExists(user.getId());
        assertEmailDoesNotExist(user.getEmail(), user.getId());
        repository.save(user);
    }

    public void assertUserExists(Long id) {
        findByIdOrThrowNotFound(id);
    }

    public void assertEmailDoesNotExist(String email) {
        repository.findByEmail(email).ifPresent(s -> {
            throw new EmailExistsException(email);
        });
    }

    public void assertEmailDoesNotExist(String email, Long id) {
        repository.findByEmailAndIdNot(email, id).ifPresent(s -> {
            throw new EmailExistsException(email);
        });
    }
}