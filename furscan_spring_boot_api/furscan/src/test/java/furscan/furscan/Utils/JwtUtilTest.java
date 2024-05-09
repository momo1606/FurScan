package furscan.furscan.Utils;

import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Repository.AuthRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static furscan.furscan.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilTest {
    @Mock
    private AuthRepository authRepository;
    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateTokenTest() {
        MstUsers user = new MstUsers();
        user.setEmail("test@gmail.com");
        String token = jwtUtil.generateToken(user);
        assertNotNull(token);
    }

    @Test
    void validateToken_ValidTokenTest() {
        String token = createValidToken();
        boolean isValid = jwtUtil.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    void validateToken_InvalidTokenTest() {
        String invalidToken = "invalidToken";
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> jwtUtil.validateToken(invalidToken));
    }

    @Test
    void validateTokenStronglyTest() {
        String token = createValidToken();
        MstUsers expectedUser = new MstUsers();
        expectedUser.setEmail("test@gmail.com");
        when(authRepository.findOneByEmail("test@gmail.com")).thenReturn(expectedUser);
        Authentication authentication = jwtUtil.validateTokenStrongly(token);
        assertEquals(expectedUser, authentication.getPrincipal());
    }

    @Test
    void getUserByTokenTest() {
        String token = createValidToken();
        MstUsers expectedUser = new MstUsers();
        expectedUser.setEmail("test@gmail.com");
        when(authRepository.findOneByEmail("test@gmail.com")).thenReturn(expectedUser);
        MstUsers user = jwtUtil.getUserByToken(token);
        assertEquals(expectedUser, user);
    }
    @Test
    void getUserByToken_UserNullTest() {
        String token = createValidToken();
        when(authRepository.findOneByEmail("test@gmail.com")).thenReturn(null);
        MstUsers user = jwtUtil.getUserByToken(token);
        assertNull(user);
    }

    @Test
    void getUserTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + createValidToken());
        MstUsers expectedUser = new MstUsers();
        expectedUser.setEmail("test@gmail.com");
        when(authRepository.findOneByEmail("test@gmail.com")).thenReturn(expectedUser);
        MstUsers user = jwtUtil.getUser(request);
        assertEquals(expectedUser, user);
    }

    private String createValidToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("first_name", "TestFirstName");
        claims.put("last_name", "TestLastName");
        long exp = System.currentTimeMillis() + DURATION_IN_MILLIS * DURATION_IN_SECONDS * DURATION_IN_MINUTES * DURATION_IN_HOURS;
        return Jwts.builder()
                .setClaims(claims)
                .setSubject("test@gmail.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(exp))
                .signWith(SignatureAlgorithm.HS256, "furscan")
                .compact();
    }
}
