package com.fifetoyi.hng_stage_two.repos;

import com.fifetoyi.hng_stage_two.model.Organisation;
import com.fifetoyi.hng_stage_two.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface OrganisationRepository extends JpaRepository<Organisation, String> {
    Optional<Organisation> findByName(String name);
}
