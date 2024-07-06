package com.fifetoyi.hng_stage_two.service;

import com.fifetoyi.hng_stage_two.domain.Organisation;
import com.fifetoyi.hng_stage_two.domain.User;
import com.fifetoyi.hng_stage_two.model.UserDTO;
import com.fifetoyi.hng_stage_two.repos.OrganisationRepository;
import com.fifetoyi.hng_stage_two.repos.UserRepository;
import com.fifetoyi.hng_stage_two.util.NotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
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

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("userId"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .collect(Collectors.toList());
    }

    public UserDTO get(final String userId) {
        return userRepository.findById(userId)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        user.setUserId(userDTO.getUserId());
        return userRepository.save(user).getUserId();
    }

    public void update(final String userId, final UserDTO userDTO) {
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final String userId) {
        userRepository.deleteById(userId);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUserId(user.getUserId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setPhone(user.getPhone());
        userDTO.setOrganisations(user.getOrganisations().stream()
                .map(organisation -> organisation.getOrgId())
                .collect(Collectors.toList()));
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        final List<Organisation> organisations = organisationRepository.findAllById(
                userDTO.getOrganisations() == null ? Collections.emptyList() : userDTO.getOrganisations());
        if (organisations.size() != (userDTO.getOrganisations() == null ? 0 : userDTO.getOrganisations().size())) {
            throw new NotFoundException("one of organisations not found");
        }
        user.setOrganisations(new HashSet<>(organisations));
        return user;
    }

    public boolean userIdExists(final String userId) {
        return userRepository.existsByUserIdIgnoreCase(userId);
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

}
