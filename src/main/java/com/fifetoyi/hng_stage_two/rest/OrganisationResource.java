package com.fifetoyi.hng_stage_two.rest;

import com.fifetoyi.hng_stage_two.model.OrganisationDTO;
import com.fifetoyi.hng_stage_two.service.OrganisationService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/organisations", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganisationResource {

    private final OrganisationService organisationService;

    public OrganisationResource(final OrganisationService organisationService) {
        this.organisationService = organisationService;
    }

    @GetMapping
    public ResponseEntity<List<OrganisationDTO>> getAllOrganisations() {
        return ResponseEntity.ok(organisationService.findAll());
    }

    @GetMapping("/{orgId}")
    public ResponseEntity<OrganisationDTO> getOrganisation(
            @PathVariable(name = "orgId") final String orgId) {
        return ResponseEntity.ok(organisationService.get(orgId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createOrganisation(
            @RequestBody @Valid final OrganisationDTO organisationDTO) {
        final String createdOrgId = organisationService.create(organisationDTO);
        return new ResponseEntity<>('"' + createdOrgId + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{orgId}")
    public ResponseEntity<String> updateOrganisation(
            @PathVariable(name = "orgId") final String orgId,
            @RequestBody @Valid final OrganisationDTO organisationDTO) {
        organisationService.update(orgId, organisationDTO);
        return ResponseEntity.ok('"' + orgId + '"');
    }

    @DeleteMapping("/{orgId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOrganisation(
            @PathVariable(name = "orgId") final String orgId) {
        organisationService.delete(orgId);
        return ResponseEntity.noContent().build();
    }

}
