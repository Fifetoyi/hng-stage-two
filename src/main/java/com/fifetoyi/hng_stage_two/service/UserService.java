package com.fifetoyi.hng_stage_two.service;

import com.fifetoyi.hng_stage_two.dto.OrganisationDTO;
import com.fifetoyi.hng_stage_two.dto.UserDTO;
import com.fifetoyi.hng_stage_two.model.Organisation;
import com.fifetoyi.hng_stage_two.model.User;
import com.fifetoyi.hng_stage_two.repos.UserRepository;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganisationService organisationService;

    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

}
