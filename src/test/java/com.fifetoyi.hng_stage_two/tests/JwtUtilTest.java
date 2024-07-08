package com.fifetoyi.hng_stage_two.tests;

import com.fifetoyi.hng_stage_two.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String secret = "test_secret";
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.setSecret(secret);
        userDetails = User.withUsername("testuser").password("password").authorities("ROLE_USER").build();
    }

    @Test
    public void testGenerateToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);

        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        assertEquals("testuser", claims.getSubject());
    }

    @Test
    public void testExtractUsername() {
        String token = jwtUtil.generateToken(userDetails);
        assertEquals("testuser", jwtUtil.extractUsername(token));
    }

    @Test
    public void testTokenExpiration() {
        String token = jwtUtil.generateToken(userDetails);
        Date expiration = jwtUtil.extractExpiration(token);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    public void testValidateToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    public void testInvalidToken() {
        String token = jwtUtil.generateToken(userDetails);
        String invalidToken = token.substring(0, token.length() - 1) + "x";

        try {
            boolean isValid = jwtUtil.validateToken(invalidToken, userDetails);
            fail("Expected SignatureException");
        } catch (SignatureException e) {
            assertNotNull(e);
        }
    }

}
