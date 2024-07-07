package com.fifetoyi.hng_stage_two.controllers;

import com.fifetoyi.hng_stage_two.dto.OrganisationDTO;
import com.fifetoyi.hng_stage_two.model.Organisation;
import com.fifetoyi.hng_stage_two.model.User;
import com.fifetoyi.hng_stage_two.service.OrganisationService;
import com.fifetoyi.hng_stage_two.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/organisations")
public class OrganisationController {

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllOrganisations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userService.findByEmail(auth.getName());

        if (userOpt.isPresent()) {
            Set<Organisation> organisations = userService.getUserOrganisations(userOpt.get().getUserId());

            List<OrganisationDTO> organisationDtos = new ArrayList<>();
            for (Organisation organisation : organisations) {
                OrganisationDTO dto = new OrganisationDTO();
                dto.setOrgId(organisation.getOrgId());
                dto.setName(organisation.getName());
                dto.setDescription(organisation.getDescription());
                organisationDtos.add(dto);
            }

            return new ResponseEntity<>(Map.of("status", "success", "message", "Organisations retrieved successfully", "data", Map.of("organisations", organisationDtos)), HttpStatus.OK);
        }

        return new ResponseEntity<>(Map.of("status", "Bad request", "message", "User not found or unauthorized", "statusCode", 400), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{orgId}")
    public ResponseEntity<?> getOrganisation(@PathVariable String orgId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userService.findByEmail(auth.getName());

        if (userOpt.isPresent()) {
            Optional<Organisation> organisationOpt = organisationService.findById(orgId);

            if (organisationOpt.isPresent() && userOpt.get().getOrganisations().contains(organisationOpt.get())) {
                Organisation organisation = organisationOpt.get();
                OrganisationDTO dto = new OrganisationDTO();
                dto.setOrgId(organisation.getOrgId());
                dto.setName(organisation.getName());
                dto.setDescription(organisation.getDescription());

                return new ResponseEntity<>(Map.of("status", "success", "message", "Organisation retrieved successfully", "data", dto), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(Map.of("status", "Bad request", "message", "Organisation not found or unauthorized", "statusCode", 400), HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<?> createOrganisation(@Validated @RequestBody OrganisationDTO organisationDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userService.findByEmail(auth.getName());

        if (userOpt.isPresent()) {
            Organisation organisation = new Organisation();
            organisation.setOrgId(UUID.randomUUID().toString());
            organisation.setName(organisationDto.getName());
            organisation.setDescription(organisationDto.getDescription());

            Organisation createdOrganisation = organisationService.createOrganisation(organisation);
            userService.addUserToOrganisation(userOpt.get().getUserId(), createdOrganisation.getOrgId());

            OrganisationDTO responseDto = new OrganisationDTO();
            responseDto.setOrgId(createdOrganisation.getOrgId());
            responseDto.setName(createdOrganisation.getName());
            responseDto.setDescription(createdOrganisation.getDescription());

            return new ResponseEntity<>(Map.of("status", "success", "message", "Organisation created successfully", "data", responseDto), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(Map.of("status", "Bad request", "message", "User not found or unauthorized", "statusCode", 400), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{orgId}/users")
    public ResponseEntity<?> addUserToOrganisation(@PathVariable String orgId, @RequestBody Map<String, String> body) {
        String userId = body.get("userId");

        userService.addUserToOrganisation(userId, orgId);

        return new ResponseEntity<>(Map.of("status", "success", "message", "User added to organisation successfully"), HttpStatus.OK);
    }
}
