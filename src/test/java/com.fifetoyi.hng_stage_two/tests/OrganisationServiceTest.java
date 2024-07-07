package com.fifetoyi.hng_stage_two.tests;

import com.fifetoyi.hng_stage_two.model.Organisation;
import com.fifetoyi.hng_stage_two.model.User;
import com.fifetoyi.hng_stage_two.repos.OrganisationRepository;
import com.fifetoyi.hng_stage_two.repos.UserRepository;
import com.fifetoyi.hng_stage_two.services.OrganisationService;
import com.fifetoyi.hng_stage_two.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrganisationServiceTest {

    @MockBean
    private OrganisationRepository organisationRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    private UserService userService;

    private User user;
    private Organisation organisation;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setEmail("test@example.com");

        organisation = new Organisation();
        organisation.setOrgId(UUID.randomUUID().toString());
        organisation.setName("Test Organisation");

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        Mockito.when(organisationRepository.findById(organisation.getOrgId())).thenReturn(Optional.of(organisation));
    }

    @Test
    public void testUserCannotAccessOtherOrganisations() {
        User otherUser = new User();
        otherUser.setUserId(UUID.randomUUID().toString());
        otherUser.setEmail("other@example.com");

        Mockito.when(userRepository.findById(otherUser.getUserId())).thenReturn(Optional.of(otherUser));
        Mockito.when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(otherUser));

        assertFalse(userService.getUserOrganisations(otherUser.getUserId()).contains(organisation));
    }

//    @Test
//    public void testUserCanAccessOwnOrganisation() {
//        userService.addUserToOrganisation(user.getUserId(), organisation.getOrgId());
//
//        Mockito.when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
//
//        Set<Organisation> userOrganisations = userService.getUserOrganisations(user.getUserId());
//        assertTrue(userOrganisations.contains(organisation));
//    }
}

