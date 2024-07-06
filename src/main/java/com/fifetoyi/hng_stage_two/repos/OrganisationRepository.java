package com.fifetoyi.hng_stage_two.repos;

import com.fifetoyi.hng_stage_two.domain.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrganisationRepository extends JpaRepository<Organisation, String> {

    boolean existsByOrgIdIgnoreCase(String orgId);

}
