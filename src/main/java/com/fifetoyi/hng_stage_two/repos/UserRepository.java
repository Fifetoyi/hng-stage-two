package com.fifetoyi.hng_stage_two.repos;

import com.fifetoyi.hng_stage_two.domain.Organisation;
import com.fifetoyi.hng_stage_two.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {

    User findFirstByOrganisations(Organisation organisation);

    List<User> findAllByOrganisations(Organisation organisation);

    boolean existsByUserIdIgnoreCase(String userId);

    boolean existsByEmailIgnoreCase(String email);

}
