package cloud.springcloudstudyuserservice.user.repository;

import cloud.springcloudstudyuserservice.user.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
