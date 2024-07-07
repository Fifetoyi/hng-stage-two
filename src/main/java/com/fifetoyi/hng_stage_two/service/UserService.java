package com.fifetoyi.hng_stage_two.service;

import com.fifetoyi.hng_stage_two.dto.OrganisationDTO;
import com.fifetoyi.hng_stage_two.dto.UserDTO;
import com.fifetoyi.hng_stage_two.model.Organisation;
import com.fifetoyi.hng_stage_two.model.User;
import com.fifetoyi.hng_stage_two.repos.UserRepository;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        Organisation organisation = new Organisation();
        organisation.setOrgId(UUID.randomUUID().toString());
        organisation.setName(user.getFirstName() + "'s Organisation");
        organisation = organisationService.createOrganisation(organisation);

        savedUser.getOrganisations().add(organisation);
        userRepository.save(savedUser);
        return savedUser;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Set<Organisation> getUserOrganisations(String userId) {
        return userRepository.findById(userId).map(User::getOrganisations).orElse(null);
    }

    public void addUserToOrganisation(String userId, String orgId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Organisation> organisationOpt = organisationService.findById(orgId);

        if (userOpt.isPresent() && organisationOpt.isPresent()) {
            User user = userOpt.get();
            Organisation organisation = organisationOpt.get();

            user.getOrganisations().add(organisation);
            userRepository.save(user);
        }
    }

}
