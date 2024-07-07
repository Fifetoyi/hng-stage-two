package com.fifetoyi.hng_stage_two.repos;

import com.fifetoyi.hng_stage_two.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, String> {
    Optional<Organisation> findByName(String name);
}
