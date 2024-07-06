package com.fifetoyi.hng_stage_two.service;

import com.fifetoyi.hng_stage_two.dto.OrganisationDTO;
import com.fifetoyi.hng_stage_two.model.Organisation;
import com.fifetoyi.hng_stage_two.repos.OrganisationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class OrganisationService {

    @Autowired
    private OrganisationRepository organisationRepository;

    public Organisation createOrganisation(OrganisationDTO organisationDto) {
        Organisation organisation = new Organisation();
        organisation.setOrgId(UUID.randomUUID().toString());
        organisation.setName(organisationDto.getName());
        organisation.setDescription(organisationDto.getDescription());

        return organisationRepository.save(organisation);
    }

    public Organisation findByOrgId(String orgId) {
        return organisationRepository.findById(orgId).orElse(null);
    }

}
