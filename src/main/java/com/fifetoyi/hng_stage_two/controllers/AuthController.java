package com.fifetoyi.hng_stage_two.controllers;

import com.fifetoyi.hng_stage_two.dto.UserLoginDto;
import com.fifetoyi.hng_stage_two.dto.UserRegistrationDto;
import com.fifetoyi.hng_stage_two.model.User;
import com.fifetoyi.hng_stage_two.services.UserService;
import com.fifetoyi.hng_stage_two.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
            // Check if userDto is null
            if (userDto == null) {
                throw new IllegalArgumentException("UserRegistrationDto cannot be null");
            }

            // Check for mandatory fields
            if (userDto.getFirstName() == null || userDto.getLastName() == null || userDto.getEmail() == null || userDto.getPassword() == null) {
                throw new IllegalArgumentException("Required fields are missing");
            }

            User user = new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setPhone(userDto.getPhone());

            // Register user
            User registeredUser = userService.registerUser(user);

            // Check if registeredUser is null
            if (registeredUser == null) {
                throw new IllegalStateException("User registration failed");
            }

            // Generate token
            String token = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                    registeredUser.getEmail(), registeredUser.getPassword(), new ArrayList<>()
            ));

            // Prepare response
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
            // Handle specific known exceptions
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle unexpected exceptions
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "An unexpected error occurred");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        List<Map<String, String>> errors = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            Map<String, String> error = new HashMap<>();
            error.put("field", fieldError.getField());
            error.put("message", fieldError.getDefaultMessage());
            errors.add(error);
        }

        errorResponse.put("errors", errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
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
