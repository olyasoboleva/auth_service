package auth.repository;

import auth.entity.UserApp;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserApp, Integer> {

    UserApp findUserAppByUsername(String username);
}
