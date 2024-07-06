package com.fifetoyi.hng_stage_two.service;

import com.fifetoyi.hng_stage_two.repos.OrganisationRepository;
import com.fifetoyi.hng_stage_two.repos.UserRepository;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;

    public UserService(final UserRepository userRepository,
            final OrganisationRepository organisationRepository) {
        this.userRepository = userRepository;
        this.organisationRepository = organisationRepository;
    }

}
