package academy.devdojo.user_service.service;

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
        return repository.save(userToSave);
    }

    public void delete(Long id) {
        var userToDelete = findByIdOrThrowNotFound(id);
        repository.delete(userToDelete);
    }

    public void update(User user) {
        findByIdOrThrowNotFound(user.getId());
        repository.save(user);
    }
}
