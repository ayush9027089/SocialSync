package social_media.social_media_handler.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import social_media.social_media_handler.entity.User;
import java.util.Optional;
public interface UserRepository extends MongoRepository<User, String> {
Optional <User> findByEmail(String email);
Optional<User> findByResetToken(String resetToken);
boolean existsByEmail(String email);
}