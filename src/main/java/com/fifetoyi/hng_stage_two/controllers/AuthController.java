package com.fifetoyi.hng_stage_two.controllers;

import com.fifetoyi.hng_stage_two.dto.UserLoginDto;
import com.fifetoyi.hng_stage_two.dto.UserRegistrationDto;
import com.fifetoyi.hng_stage_two.exceptions.DuplicateResourceException;
import com.fifetoyi.hng_stage_two.model.User;
import com.fifetoyi.hng_stage_two.services.UserService;
import com.fifetoyi.hng_stage_two.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody UserRegistrationDto userDto) {
        try {
            if (userDto == null) {
                throw new IllegalArgumentException("UserRegistrationDto cannot be null");
            }
            if (userDto.getFirstName() == null || userDto.getLastName() == null || userDto.getEmail() == null || userDto.getPassword() == null) {
                throw new IllegalArgumentException("Required fields are missing");
            }
            if (userService.findByEmail(userDto.getEmail()).isPresent()) {
                throw new DuplicateResourceException("Email already registered");
            }

            User user = new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setPhone(userDto.getPhone());

            User registeredUser = userService.registerUser(user);
            if (registeredUser == null) {
                throw new IllegalStateException("User registration failed");
            }

            String token = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                    registeredUser.getEmail(), registeredUser.getPassword(), new ArrayList<>()
            ));

            UserRegistrationDto responseUserDto = new UserRegistrationDto();
            responseUserDto.setUserId(registeredUser.getUserId());
            responseUserDto.setFirstName(registeredUser.getFirstName());
            responseUserDto.setLastName(registeredUser.getLastName());
            responseUserDto.setEmail(registeredUser.getEmail());
            responseUserDto.setPhone(registeredUser.getPhone());

            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", token);
            data.put("user", responseUserDto);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Registration successful");
            response.put("data", data);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> login(@Validated @RequestBody UserLoginDto loginDto) {
        Optional<User> userOpt = userService.findByEmail(loginDto.getEmail());

        if (userOpt.isPresent() && passwordEncoder.matches(loginDto.getPassword(), userOpt.get().getPassword())) {
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(userOpt.get().getEmail(), userOpt.get().getPassword(), new ArrayList<>());
            String token = jwtUtil.generateToken(userDetails);

            UserRegistrationDto responseUserDto = new UserRegistrationDto();
            responseUserDto.setUserId(userOpt.get().getUserId());
            responseUserDto.setFirstName(userOpt.get().getFirstName());
            responseUserDto.setLastName(userOpt.get().getLastName());
            responseUserDto.setEmail(userOpt.get().getEmail());
            responseUserDto.setPhone(userOpt.get().getPhone());

            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", token);
            data.put("user", responseUserDto);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Login successful");
            response.put("data", data);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(Map.of("status", "Bad request", "message", "Authentication failed", "statusCode", 401), HttpStatus.UNAUTHORIZED);
    }
}
