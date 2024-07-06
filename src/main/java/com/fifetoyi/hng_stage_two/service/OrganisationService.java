package com.fifetoyi.hng_stage_two.service;

import com.fifetoyi.hng_stage_two.domain.Organisation;
import com.fifetoyi.hng_stage_two.model.OrganisationDTO;
import com.fifetoyi.hng_stage_two.repos.OrganisationRepository;
import com.fifetoyi.hng_stage_two.repos.UserRepository;
import com.fifetoyi.hng_stage_two.util.NotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
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

    public List<OrganisationDTO> findAll() {
        final List<Organisation> organisations = organisationRepository.findAll(Sort.by("orgId"));
        return organisations.stream()
                .map(organisation -> mapToDTO(organisation, new OrganisationDTO()))
                .collect(Collectors.toList());
    }

    public OrganisationDTO get(final String orgId) {
        return organisationRepository.findById(orgId)
                .map(organisation -> mapToDTO(organisation, new OrganisationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final OrganisationDTO organisationDTO) {
        final Organisation organisation = new Organisation();
        mapToEntity(organisationDTO, organisation);
        organisation.setOrgId(organisationDTO.getOrgId());
        return organisationRepository.save(organisation).getOrgId();
    }

    public void update(final String orgId, final OrganisationDTO organisationDTO) {
        final Organisation organisation = organisationRepository.findById(orgId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(organisationDTO, organisation);
        organisationRepository.save(organisation);
    }

    public void delete(final String orgId) {
        final Organisation organisation = organisationRepository.findById(orgId)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        userRepository.findAllByOrganisations(organisation)
                .forEach(user -> user.getOrganisations().remove(organisation));
        organisationRepository.delete(organisation);
    }

    private OrganisationDTO mapToDTO(final Organisation organisation,
            final OrganisationDTO organisationDTO) {
        organisationDTO.setOrgId(organisation.getOrgId());
        organisationDTO.setName(organisation.getName());
        organisationDTO.setDescription(organisation.getDescription());
        return organisationDTO;
    }

    private Organisation mapToEntity(final OrganisationDTO organisationDTO,
            final Organisation organisation) {
        organisation.setName(organisationDTO.getName());
        organisation.setDescription(organisationDTO.getDescription());
        return organisation;
    }

    public boolean orgIdExists(final String orgId) {
        return organisationRepository.existsByOrgIdIgnoreCase(orgId);
    }

}
