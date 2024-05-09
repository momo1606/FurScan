package furscan.furscan.Services;

import furscan.furscan.DTO.LoginRequestDto;
import furscan.furscan.DTO.SignupRequestDto;
import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Repository.AuthRepository;
import furscan.furscan.Utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static furscan.furscan.Constants.MSTUSERS_OTP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private SignupRequestDto signupRequestDto;

    @Mock
    private LoginRequestDto loginRequestDto;

    @Mock
    private MstUsers mst_users;

    @Mock
    private MstUsers expectedSignUpResponse;

    @Mock
    private Map<String, Object> expectedLoginResponse;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);

        //Initializing SignupRequestDto
        signupRequestDto = SignupRequestDto.builder().first_name("John").last_name("Doe").email("johndoe@gmail.com").password("qwerty").phone_no("9876543210").profile_image_text("mockImageText.txt").is_doctor(0).is_active(1).build();

        //Initializing the MstUsers
        mst_users = new MstUsers(1, "John", "Doe", "johndoe@gmail.com", "qwerty", "9876543210", "testImage", 0, 1, LocalDateTime.now(), null, LocalDateTime.now(), MSTUSERS_OTP);

        //Initializing LoginRequestDto
        loginRequestDto = LoginRequestDto.builder().first_name("John").last_name("Doe").email("johndoe@gmail.com").password("qwerty").build();
    }

    @Test
    public void signUp_ReturnSuccess() {
        when(authRepository.save(any(MstUsers.class))).thenReturn(mst_users);
        expectedSignUpResponse = authService.signUp(signupRequestDto);

        // Verify that the save method was called with the correct parameters
        verify(authRepository, times(1)).save(argThat(user ->
                user.getFirst_name().equals(signupRequestDto.getFirst_name())
                        && user.getLast_name().equals(signupRequestDto.getLast_name())
                        && user.getEmail().equals(signupRequestDto.getEmail())
                        && user.getPassword().equals(signupRequestDto.getPassword())
                        && user.getPhone_no().equals(signupRequestDto.getPhone_no())
                        && user.getProfile_image_text().equals(signupRequestDto.getProfile_image_text())
                        && user.getIs_active().equals(signupRequestDto.getIs_active())
                        && user.getIs_doctor().equals(signupRequestDto.getIs_doctor())
        ));
        assertEquals(expectedSignUpResponse, mst_users);
    }

    @Test
    public void login_ReturnSuccess(){
        String token = "";
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        when(authRepository.findOneByEmail(loginRequestDto.getEmail())).thenReturn(mst_users);
        when(authenticationManager.authenticate(authToken)).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtUtil.generateToken(mst_users)).thenReturn(token);

        expectedLoginResponse = new HashMap<>();
        expectedLoginResponse.put("accessToken", token);
        expectedLoginResponse.put("user", mst_users);

        Map<String, Object> result = authService.login(loginRequestDto);

        assertEquals(expectedLoginResponse, result);
    }

    @Test
    public void login_ReturnNull(){
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

        when(authRepository.findOneByEmail(loginRequestDto.getEmail())).thenReturn(mst_users);
        when(authenticationManager.authenticate(authToken)).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        expectedLoginResponse = null;

        Map<String, Object> result = authService.login(loginRequestDto);

        assertEquals(expectedLoginResponse, result);
    }

}
