package furscan.furscan.Controller;

import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Services.UserService;
import furscan.furscan.Utils.BadRequest;
import furscan.furscan.Utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static furscan.furscan.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    List<MstUsers> mstUsersList;

    @Mock
    MstUsers mstUsers;

    @Mock
    Optional<MstUsers> mstUsersOptional;

    @Mock
    ResponseEntity<Map<String, Object>> expectedSuccessResponse;

    @Mock
    ResponseEntity<Map<String, Object>> expectedErrorResponse;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    Map<String, Object> meId;

    private ResponseEntity<Map<String, Object>> actualResponse;
    Integer id;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);

        //Initializing id
        id = 1;
        //Initializing MstUsers
        mstUsers = new MstUsers(USER_ID, "John", "Doe", "johndoe@gmail.com", "qwerty", "9876543210", "testImageText", 0, 1, LocalDateTime.now(), null, LocalDateTime.now(), MSTUSERS_OTP);
    }

    @Test
    public void getMstUsers_ReturnSuccess(){
        when(userService.getUser()).thenReturn(mstUsersList);
        expectedSuccessResponse = ResponseUtil.successResponse(mstUsersList);
        actualResponse = userController.getMstUsers();
        assertEquals(expectedSuccessResponse, actualResponse);
    }

    @Test
    public void getMstUsers_ReturnError(){
        when(userService.getUser()).thenReturn(mstUsersList);
        //Setting userList to empty
        when(mstUsersList.isEmpty()).thenReturn(true);
        expectedErrorResponse = ResponseUtil.errorResponse("No users found", HttpStatus.NOT_FOUND.value());
        actualResponse = userController.getMstUsers();
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    public void createUsers_ReturnSuccess(){
        when(userService.createUsers(mstUsers)).thenReturn(mstUsers);
        expectedSuccessResponse = ResponseUtil.successResponse(mstUsers);
        actualResponse = userController.createUsers(mstUsers);
        assertEquals(expectedSuccessResponse, actualResponse);
    }

    @Test
    public void createUsers_ReturnBadRequestError(){
        //Mocking the Exception
        BadRequest e = mock(BadRequest.class);

        when(userService.createUsers(mstUsers)).thenThrow(e);
        expectedErrorResponse = ResponseUtil.badRequest(e, HttpStatus.BAD_REQUEST.value());
        actualResponse = userController.createUsers(mstUsers);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    public void createUsers_ReturnRuntimeExceptionError(){
        //Mocking the Exception
        RuntimeException e = mock(RuntimeException.class);
        String msg = "Internal Server Error" + e.getMessage();

        when(userService.createUsers(mstUsers)).thenThrow(e);
        expectedErrorResponse = ResponseUtil.errorResponse(msg, HttpStatus.INTERNAL_SERVER_ERROR.value());
        actualResponse = userController.createUsers(mstUsers);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    public void test_getUserByID(){
        when(userController.getUserByID(id)).thenReturn(mstUsersOptional);
        assertEquals(mstUsersOptional, userController.getUserByID(id));
    }

    @Test
    public void updateUser_ReturnSuccess(){
        when(userService.updateUser(id, mstUsers)).thenReturn(mstUsersOptional);
        expectedSuccessResponse = ResponseUtil.successResponse(mstUsersOptional.get());
        actualResponse = userController.updateUser(id, mstUsers);
        assertEquals(expectedSuccessResponse, actualResponse);
    }

    @Test
    public void updateUser_ReturnUserNotFoundError(){
        when(userService.updateUser(id, mstUsers)).thenReturn(mstUsersOptional);
        //Setting mstUser to empty
        when(mstUsersOptional.isEmpty()).thenReturn(true);
        expectedErrorResponse = ResponseUtil.errorResponse("User Not found", HttpStatus.NOT_FOUND.value());
        actualResponse = userController.updateUser(id, mstUsers);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    public void updateUser_ReturnInternalServerException(){
//        when(userService.updateUser(id, mstUsers)).thenThrow(new RuntimeException());
        RuntimeException e = mock(RuntimeException.class);
        when(userService.updateUser(id, mstUsers)).thenThrow(e);
        String msg = "Internal Server Error " + e.getMessage();
        expectedErrorResponse = ResponseUtil.errorResponse(msg, HttpStatus.INTERNAL_SERVER_ERROR.value());
        actualResponse = userController.updateUser(id, mstUsers);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    public void deleteUser_ReturnSuccess(){
        when(userService.deletedUser(id)).thenReturn(mstUsersOptional);
        expectedSuccessResponse = ResponseUtil.successResponse(mstUsersOptional.get());
        actualResponse = userController.deleteUser(id);
        assertEquals(expectedSuccessResponse, actualResponse);
    }

    @Test
    public void deleteUser_ReturnUserNotDeletedError(){
        when(userService.deletedUser(id)).thenReturn(mstUsersOptional);
        //Setting mstUser to empty
        when(mstUsersOptional.isEmpty()).thenReturn(true);
        expectedErrorResponse = ResponseUtil.errorResponse("Failed to delete", HttpStatus.NOT_FOUND.value());
        actualResponse = userController.deleteUser(id);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    public void me_ReturnSuccess(){
        when(userService.me(httpServletRequest)).thenReturn(meId);
        expectedSuccessResponse = ResponseUtil.successResponse(meId);
        actualResponse = userController.me(httpServletRequest);
        assertEquals(expectedSuccessResponse, actualResponse);
    }

    @Test
    public void me_ReturnNoUserFoundError(){
        when(userService.me(httpServletRequest)).thenReturn(null);
        expectedErrorResponse = ResponseUtil.errorResponse("No user found", HttpStatus.NOT_FOUND.value());
        actualResponse = userController.me(httpServletRequest);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    void sendEmailForOtp_ReturnsSuccessResponse() throws Exception {
        when(userService.sendEmailForOtp(anyString())).thenReturn(meId);
        expectedSuccessResponse = ResponseUtil.successResponse(meId);
        actualResponse = userController.sendEmailForOtp("rh917388@dal.ca");
        assertEquals(expectedSuccessResponse, actualResponse);
    }

    @Test
    void sendEmailForOtp_ReturnsNotFoundResponse() throws Exception {
        when(userService.sendEmailForOtp(anyString())).thenReturn(null);
        when(meId.isEmpty()).thenReturn(true);
        expectedErrorResponse = ResponseUtil.errorResponse("No user found", HttpStatus.NOT_FOUND.value());
        actualResponse = userController.sendEmailForOtp("rh91788@dal.ca");
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    void sendEmailForOtp_ReturnsErrorResponse() throws Exception {
        RuntimeException e = mock(RuntimeException.class);
        when(userService.sendEmailForOtp(anyString())).thenThrow(e);
        String msg = "Internal Server Error" + e.getMessage();
        expectedErrorResponse = ResponseUtil.errorResponse(msg, HttpStatus.INTERNAL_SERVER_ERROR.value());
        actualResponse = userController.sendEmailForOtp("rh917388@dal.ca");
        assertEquals(expectedErrorResponse, actualResponse);

    }

    @Test
    void otpVerification_ReturnsSuccessResponse() throws Exception {
        when(userService.otpVerification(anyString(), any(Integer.class))).thenReturn(OTP_SUCCESS_RES);
        expectedSuccessResponse = ResponseUtil.successResponse(OTP_SUCCESS_RES);
        actualResponse = userController.otpVerification("rh917388@dal.ca", MSTUSERS_OTP);
        assertEquals(expectedSuccessResponse, actualResponse);    
    }

    @Test
    void otpVerification_ReturnsUserNotFoundError() throws Exception {
        when(userService.otpVerification(anyString(), any(Integer.class))).thenReturn(1);
        expectedErrorResponse = ResponseUtil.errorResponse("User not found", HttpStatus.NOT_FOUND.value());
        actualResponse = userController.otpVerification("rh91788@dal.ca", MSTUSERS_OTP);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    void otpVerification_ReturnsFailedToVerifyError() throws Exception {
        when(userService.otpVerification(anyString(), any(Integer.class))).thenReturn(OTP_ERROR_RES);
        expectedErrorResponse = ResponseUtil.errorResponse("Failed to verify the OTP", HttpStatus.NOT_FOUND.value());
        actualResponse = userController.otpVerification("rh91788@dal.ca", MSTUSERS_OTP);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    void otpVerification_ReturnsInternalServerError() throws Exception {
        RuntimeException e = mock(RuntimeException.class);
        when(userService.otpVerification(anyString(), any(Integer.class))).thenThrow(e);
        String msg = "Internal Server Error " + e.getMessage();
        expectedErrorResponse = ResponseUtil.errorResponse(msg, HttpStatus.INTERNAL_SERVER_ERROR.value());
        actualResponse = userController.otpVerification("rh91788@dal.ca", MSTUSERS_OTP);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    void resetPassword_ReturnsUserNotFoundError() throws Exception {
        when(userService.resetPassword(anyString(), any(String.class))).thenReturn(1);
        expectedErrorResponse = ResponseUtil.errorResponse("User not found", HttpStatus.NOT_FOUND.value());
        actualResponse = userController.resetPassword("rh91788@dal.ca", "12345");
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    void resetPassword_ReturnsSuccessResponse() throws Exception {
        when(userService.resetPassword(anyString(), any(String.class))).thenReturn(OTP_SUCCESS_RES);
        expectedSuccessResponse = ResponseUtil.successResponse(OTP_SUCCESS_RES);
        actualResponse = userController.resetPassword("rh917388@dal.ca", "12345");
        assertEquals(expectedSuccessResponse, actualResponse); 
    }

    @Test
    void resetPassword_ReturnsInternalServerError() throws Exception {
        RuntimeException e = mock(RuntimeException.class);
        when(userService.resetPassword(anyString(), any(String.class))).thenThrow(e);
        String msg = "Internal Server Error " + e.getMessage();
        expectedErrorResponse = ResponseUtil.errorResponse(msg, HttpStatus.INTERNAL_SERVER_ERROR.value());
        actualResponse = userController.resetPassword("rh91788@dal.ca", "12345");
        assertEquals(expectedErrorResponse, actualResponse);
    }
}
