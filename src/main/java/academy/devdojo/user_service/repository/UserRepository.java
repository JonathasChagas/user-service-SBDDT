package academy.devdojo.user_service.repository;

import academy.devdojo.user_service.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByFirstNameIgnoreCase(String name);
}
