package org.bibblan.usermanagement.userrepository;

import org.bibblan.usermanagement.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
