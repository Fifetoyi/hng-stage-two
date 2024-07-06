package com.fifetoyi.hng_stage_two.repos;

import com.fifetoyi.hng_stage_two.model.Organisation;
import com.fifetoyi.hng_stage_two.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrganisationRepository extends JpaRepository<Organisation, String> {
    List<Organisation> findByUsersContaining(User user);
}
