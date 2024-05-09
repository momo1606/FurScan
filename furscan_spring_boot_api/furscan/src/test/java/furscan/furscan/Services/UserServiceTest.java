package furscan.furscan.Services;

import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Repository.UserRepository;
import furscan.furscan.Utils.BadRequest;
import furscan.furscan.Utils.EmailSenderUtil;
import furscan.furscan.Utils.JwtUtil;
import furscan.furscan.Validator.UserValidation;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static furscan.furscan.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidation userValidation;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private EmailSenderUtil senderUtil;


    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUser_NullTest(){
        List<MstUsers> mst_user = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(mst_user);
        List<MstUsers> result = userService.getUser();
        assertNull(result);
    }

    @Test
    public void getUserTest(){
        List<MstUsers> mst_user = List.of(new MstUsers(),new MstUsers());
        when(userRepository.findAll()).thenReturn(mst_user);
        List<MstUsers> result = userService.getUser();
        assertEquals(mst_user.size(), result.size());
    }

    @Test
    void createUsers_SuccessfulTest() {
        MstUsers inputUser = new MstUsers();
        inputUser.setEmail("test@gmail.com");
        inputUser.setPassword("password");
        when(userValidation.createUpdateRequest(any())).thenReturn(new ArrayList<>());
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(inputUser);

        MstUsers resultUser = userService.createUsers(inputUser);

        assertEquals("test@gmail.com", resultUser.getEmail());
        assertEquals("encodedPassword", resultUser.getPassword());
    }

    @Test
    void createUsers_WithValidationErrorsTest() {
        MstUsers inputUser = new MstUsers();
        List<Error> validationErrors = new ArrayList<>();
        Error error = new Error("Bad Request");
        validationErrors.add(error);
        doReturn(validationErrors).when(userValidation).createUpdateRequest(any());
        assertThrows(BadRequest.class, () -> userService.createUsers(inputUser));
    }

    @Test
    public void getUserByIDTest(){
        MstUsers user = new MstUsers();
        user.setUser_id(1);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        Optional<MstUsers> result = userService.getUserByID(1);
        assertEquals(Optional.of(user),result);
    }

    @Test
    public void updateUserTest(){
        MstUsers user = new MstUsers();
        user.setUser_id(1);
        Optional<MstUsers> optionalUser = Optional.of(user);
        when(userRepository.findById(anyInt())).thenReturn(optionalUser);
        Optional<MstUsers> result = userService.updateUser(1, user);
        assertEquals(optionalUser,result);
    }
    @Test
    void deletedUserTest() {
        int userId = 1;
        MstUsers user = new MstUsers();
        user.setUser_id(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Optional<MstUsers> result = userService.deletedUser(userId);
        assertEquals(user, result.get());
    }

    @Test
    public void meTest(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        MstUsers mockUser = new MstUsers();
        mockUser.setUser_id(1);
        mockUser.setEmail("testUser");
        Map<String, Object> mockPet1 = createMockPetDetails(1, "Dog", "Healthy");
        Map<String, Object> mockPet2 = createMockPetDetails(ID2, "Cat", "Sick");
        when(jwtUtil.getUser(request)).thenReturn(mockUser);
        List<Map<String, Object>> mockPetDetails = Arrays.asList(mockPet1, mockPet2);
        when(userRepository.petList(1)).thenReturn(mockPetDetails);

        Map<String, Object> result = userService.me(request);

        MstUsers resultUser = (MstUsers) result.get("user");

        List<Map<String, Object>> resultPetDetails = (List<Map<String, Object>>) result.get("petReport");
        assertEquals(mockPetDetails, resultPetDetails);
        verify(jwtUtil, times(1)).getUser(request);
        verify(userRepository, times(1)).petList(1);
    }

    private Map<String, Object> createMockPetDetails(int id, String petName, String healthStatus) {
        Map<String, Object> petDetails = new HashMap<>();
        petDetails.put("id", id);
        petDetails.put("petName", petName);
        petDetails.put("healthStatus", healthStatus);
        return petDetails;
    }
    @Test
    void sendEmailForOtp_EmailNotFoundTest() {
        when(userRepository.findOneByEmail(anyString())).thenReturn(null);
        Map<String, Object> result = userService.sendEmailForOtp("nonexistent@email.com");
        assertNull(result.get("email"));
        verify(userRepository, never()).save(any());
        verify(senderUtil, never()).sendEmailWithoutAttachment(anyString(), anyString(), anyString());
    }

    @Test
    void sendEmailForOtp_EmailFoundTest() {
        MstUsers user = new MstUsers();
        user.setEmail("test@gmail.com");
        when(userRepository.findOneByEmail(anyString())).thenReturn(user);
        Map<String, Object> result = userService.sendEmailForOtp("test@email.com");
        assertEquals("test@email.com", result.get("email"));
        verify(userRepository, times(1)).save(any(MstUsers.class));
        verify(senderUtil, times(1)).sendEmailWithoutAttachment(anyString(), anyString(), anyString());
    }

    @Test
    public void otpVerificationTest(){
        MstUsers user = new MstUsers();
        user.setEmail("test@gmail.com");
        user.setOtp(MSTUSERS_OTP);
        when(userRepository.findOneByEmail("test@gmail.com")).thenReturn(user);
        int result = userService.otpVerification("test@gmail.com", MSTUSERS_OTP);
        assertEquals(OTP_SUCCESS_RES, result);
    }

    @Test
    public void otpVerification_NotFoundTest(){
        MstUsers user = new MstUsers();
        user.setEmail("test@gmail.com");
        user.setOtp(MSTUSERS_OTP);
        when(userRepository.findOneByEmail("test@gmail.com")).thenReturn(null);
        int result = userService.otpVerification("test@gmail.com", MSTUSERS_OTP);
        assertEquals(1, result);
    }

    @Test
    public void otpVerification_WrongOtpTest(){
        MstUsers user = new MstUsers();
        user.setEmail("test@gmail.com");
        user.setOtp(MSTUSERS_OTP);
        when(userRepository.findOneByEmail("test@gmail.com")).thenReturn(user);
        int result = userService.otpVerification("test@gmail.com", INVALID_OTP);
        assertEquals(OTP_ERROR_RES, result);
    }

    @Test
    public void resetPasswordTest(){
        MstUsers user = new MstUsers();
        user.setEmail("test@gmail.com");
        user.setPassword("password");
        when(userRepository.findOneByEmail(anyString())).thenReturn(user);
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        int result = userService.resetPassword("test@gmail.com","password");
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    public void resetPassword_NotFoundTest() {
        when(userRepository.findOneByEmail(anyString())).thenReturn(null);
        int result = userService.resetPassword("test@gmail.com", "password");
        assertEquals(1, result);
        verifyNoInteractions(bCryptPasswordEncoder);
    }

    @Test
    void triggerEmailTest() {
        String to = "test@example.com";
        String body = "Test email body";
        String subject = "Test subject";

        userService.triggerEmail(to, body, subject);

        verify(senderUtil, times(1)).sendEmailWithoutAttachment(to, body, subject);
    }
}

