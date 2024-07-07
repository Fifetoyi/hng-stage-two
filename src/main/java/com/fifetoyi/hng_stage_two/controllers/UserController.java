package com.fifetoyi.hng_stage_two.controllers;

import com.fifetoyi.hng_stage_two.dto.UserDTO;
import com.fifetoyi.hng_stage_two.model.User;
import com.fifetoyi.hng_stage_two.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userService.findByEmail(auth.getName());

        if (userOpt.isPresent() && userOpt.get().getUserId().equals(id)) {
            User user = userOpt.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());

            return new ResponseEntity<>(Map.of("status", "success", "message", "User retrieved successfully", "data", userDTO), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("status", "Bad request", "message", "User not found or unauthorized", "statusCode", 400), HttpStatus.BAD_REQUEST);
    }
}
