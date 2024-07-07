package com.fifetoyi.hng_stage_two.services;

import com.fifetoyi.hng_stage_two.model.Organisation;
import com.fifetoyi.hng_stage_two.repos.OrganisationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class OrganisationService {

    @Autowired
    private OrganisationRepository organisationRepository;

    public Organisation createOrganisation(Organisation organisation) {
        return organisationRepository.save(organisation);
    }

    public Optional<Organisation> findById(String orgId) {
        return organisationRepository.findById(orgId);
    }

}
