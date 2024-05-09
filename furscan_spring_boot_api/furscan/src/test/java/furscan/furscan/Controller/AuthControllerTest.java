package furscan.furscan.Controller;

import furscan.furscan.DTO.LoginRequestDto;
import furscan.furscan.DTO.SignupRequestDto;
import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Services.AuthService;
import furscan.furscan.Utils.ResponseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static furscan.furscan.Constants.INTERNAL_SERVER_ERROR_VALUE;
import static furscan.furscan.Constants.MSTUSERS_OTP;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;


public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private MstUsers mstUsers;

    @Mock
    private SignupRequestDto signupRequestDto;

    @Mock
    private LoginRequestDto loginRequestDto;

    @Mock
    Map<String, Object> auth;

    @Mock
    private ResponseEntity<Map<String, Object>> expectedSuccessResponse;

    @Mock
    private ResponseEntity<Map<String, Object>> expectedErrorResponse;

    private ResponseEntity<Map<String, Object>> actualResponse;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);

        //Initializing SignupRequestDto
        signupRequestDto = SignupRequestDto
                .builder()
                .first_name("John")
                .last_name("Doe")
                .email("johndoe@gmail.com")
                .password("qwerty")
                .phone_no("9876543210")
                .profile_image_text("mockImageText.txt")
                .is_doctor(0)
                .is_active(1)
                .build();

        //Initializing the MstUsers
        mstUsers = new MstUsers(1, "John", "Doe", "johndoe@gmail.com", "qwerty", "9876543210", "testImage", 0, 1, LocalDateTime.now(), null, LocalDateTime.now(), MSTUSERS_OTP);

        //Initializing LoginRequestDto
        loginRequestDto = LoginRequestDto
                .builder()
                .first_name("John")
                .last_name("Doe")
                .email("johndoe@gmail.com")
                .password("qwerty")
                .build();
    }

    @Test
    public void signUp_ReturnSuccess(){
        expectedSuccessResponse = ResponseUtil.successResponse(mstUsers);
        when(authService.signUp(signupRequestDto)).thenReturn(mstUsers);
        actualResponse = authController.signUp(signupRequestDto);
        assertEquals(expectedSuccessResponse, actualResponse);
    }

    @Test
    public void signUp_ReturnError(){
        expectedErrorResponse = ResponseUtil.errorResponse("Internal Server Error", INTERNAL_SERVER_ERROR_VALUE);
        when(authService.signUp(signupRequestDto)).thenThrow(new RuntimeException());
        actualResponse = authController.signUp(signupRequestDto);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    public void login_AuthNotNull_ReturnLoginSuccess(){
        expectedSuccessResponse = ResponseUtil.successResponse(auth);
        when(authService.login(loginRequestDto)).thenReturn(auth);
        actualResponse = authController.login(loginRequestDto);
        assertEquals(expectedSuccessResponse, actualResponse);
    }

    @Test
    public void login_AuthNull_ReturnLoginFailed(){
        when(authService.login(loginRequestDto)).thenReturn(null);
        expectedErrorResponse = ResponseUtil.errorResponse("Login failed", HttpStatus.UNAUTHORIZED.value());
        actualResponse = authController.login(loginRequestDto);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    public void login_ReturnError(){
        //Mocking the Exception
        Exception e = mock(Exception.class);
        when(e.getMessage()).thenReturn("Internal server error");

        when(authService.login(loginRequestDto)).thenThrow(new RuntimeException("Internal server error"));
        expectedErrorResponse = ResponseUtil.errorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        actualResponse = authController.login(loginRequestDto);
        assertEquals(expectedErrorResponse, actualResponse);
    }
}
