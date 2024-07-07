package com.fifetoyi.hng_stage_two.controllers;

import com.fifetoyi.hng_stage_two.dto.UserDTO;
import com.fifetoyi.hng_stage_two.dto.UserLoginDto;
import com.fifetoyi.hng_stage_two.dto.UserRegistrationDto;
import com.fifetoyi.hng_stage_two.model.User;
import com.fifetoyi.hng_stage_two.service.UserService;
import com.fifetoyi.hng_stage_two.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhone(userDto.getPhone());

        User registeredUser = userService.registerUser(user);
        String token = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(registeredUser.getEmail(), registeredUser.getPassword(), new ArrayList<>()));

        UserDTO responseUserDto = new UserDTO();
        responseUserDto.setUserId(registeredUser.getUserId());
        responseUserDto.setFirstName(registeredUser.getFirstName());
        responseUserDto.setLastName(registeredUser.getLastName());
        responseUserDto.setEmail(registeredUser.getEmail());
        responseUserDto.setPhone(registeredUser.getPhone());

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Registration successful");
        response.put("accessToken", token);
        response.put("user", responseUserDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody UserLoginDto loginDto) {
        Optional<User> userOpt = userService.findByEmail(loginDto.getEmail());

        if (userOpt.isPresent() && passwordEncoder.matches(loginDto.getPassword(), userOpt.get().getPassword())) {
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(userOpt.get().getEmail(), userOpt.get().getPassword(), new ArrayList<>());
            String token = jwtUtil.generateToken(userDetails);

            UserDTO responseUserDto = new UserDTO();
            responseUserDto.setUserId(userOpt.get().getUserId());
            responseUserDto.setFirstName(userOpt.get().getFirstName());
            responseUserDto.setLastName(userOpt.get().getLastName());
            responseUserDto.setEmail(userOpt.get().getEmail());
            responseUserDto.setPhone(userOpt.get().getPhone());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Login successful");
            response.put("accessToken", token);
            response.put("user", responseUserDto);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(Map.of("status", "Bad request", "message", "Authentication failed", "statusCode", 401), HttpStatus.UNAUTHORIZED);
    }
}
