package org.bibblan.usermanagement.userrepository;

import org.bibblan.usermanagement.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
