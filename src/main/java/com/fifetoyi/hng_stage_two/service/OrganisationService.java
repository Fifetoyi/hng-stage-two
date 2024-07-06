package com.fifetoyi.hng_stage_two.service;

import com.fifetoyi.hng_stage_two.repos.OrganisationRepository;
import com.fifetoyi.hng_stage_two.repos.UserRepository;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;


@Service
@Transactional
public class OrganisationService {

    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;

    public OrganisationService(final OrganisationRepository organisationRepository,
            final UserRepository userRepository) {
        this.organisationRepository = organisationRepository;
        this.userRepository = userRepository;
    }

}
