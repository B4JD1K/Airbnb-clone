package pl.b4jd1k.airbnb_clone_back.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.b4jd1k.airbnb_clone_back.user.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findOneByEmail(String email);

  Optional<User> findOneByPublicId(UUID publicId);
}
