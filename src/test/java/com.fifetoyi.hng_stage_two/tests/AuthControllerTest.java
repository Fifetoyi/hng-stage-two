package com.fifetoyi.hng_stage_two.tests;

import com.fifetoyi.hng_stage_two.dto.UserRegistrationDto;
import com.fifetoyi.hng_stage_two.model.User;
import com.fifetoyi.hng_stage_two.services.UserService;
import com.fifetoyi.hng_stage_two.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    private UserRegistrationDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserRegistrationDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john@example.com");
        userDto.setPassword("password");
        userDto.setPhone("1234567890");
    }

    @Test
    public void testRegisterUserSuccessfully() throws Exception {
        User mockUser = new User();
        mockUser.setUserId("mock-user-id");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john@example.com");
        mockUser.setPassword("encoded-password");
        mockUser.setPhone("1234567890");
        mockUser.setOrganisations(new HashSet<>());

        Mockito.when(userService.registerUser(Mockito.any())).thenReturn(mockUser);
        Mockito.when(jwtUtil.generateToken(Mockito.any())).thenReturn("dummy-token");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john@example.com\", \"password\": \"password\", \"phone\": \"1234567890\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("Registration successful")))
                .andExpect(jsonPath("$.data.accessToken", is("dummy-token")))
                .andExpect(jsonPath("$.data.user.firstName", is("John")))
                .andExpect(jsonPath("$.data.user.lastName", is("Doe")))
                .andExpect(jsonPath("$.data.user.email", is("john@example.com")))
                .andExpect(jsonPath("$.data.user.phone", is("1234567890")));
    }


    @Test
    public void testRegisterUserMissingFields() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\"}")) // Missing email and password
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors[*].field", hasItem("email")))
                .andExpect(jsonPath("$.errors[*].message", hasItem(containsString("Email is required"))))
                .andExpect(jsonPath("$.errors[*].field", hasItem("password")))
                .andExpect(jsonPath("$.errors[*].message", hasItem(containsString("Password is required"))));
    }

    @Test
    public void testRegisterUserDuplicateEmail() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Duplicate email")).when(userService).registerUser(Mockito.any());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john@example.com\", \"password\": \"password\", \"phone\": \"1234567890\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Duplicate email")));
    }

    @Test
    public void testLoginUserSuccessfully() throws Exception {
        User mockUser = new User();
        mockUser.setUserId("mock-user-id");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john@example.com");
        mockUser.setPassword("encoded-password");
        mockUser.setPhone("1234567890");
        mockUser.setOrganisations(new HashSet<>());

        Mockito.when(userService.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        Mockito.when(passwordEncoder.matches("password", mockUser.getPassword())).thenReturn(true);
        Mockito.when(jwtUtil.generateToken(Mockito.any())).thenReturn("dummy-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"john@example.com\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("Login successful")))
                .andExpect(jsonPath("$.data.accessToken", is("dummy-token")))
                .andExpect(jsonPath("$.data.user.firstName", is("John")))
                .andExpect(jsonPath("$.data.user.lastName", is("Doe")))
                .andExpect(jsonPath("$.data.user.email", is("john@example.com")))
                .andExpect(jsonPath("$.data.user.phone", is("1234567890")));
    }

}

