package furscan.furscan.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Services.UserService;
import furscan.furscan.Utils.BadRequest;
import furscan.furscan.Utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static furscan.furscan.Constants.OTP_ERROR_RES;


@RestController
public class UserController {
     
     @Autowired
     private UserService userService;

     /**
      * This function is for getting all the Users list
      * @return
      */
     @RequestMapping(value = "user-list")
     public ResponseEntity<Map<String, Object>> getMstUsers() {
          List<MstUsers> users = userService.getUser();
          if(users.isEmpty()){
               return ResponseUtil.errorResponse("No users found", HttpStatus.NOT_FOUND.value());
          }
          return ResponseUtil.successResponse(users);
     }

     /**
      * This function is for creating the users or doctors
      * @param mstUsers
      * @return
      */
     @RequestMapping(value="create-user", method=RequestMethod.POST)
     public ResponseEntity<Map<String, Object>> createUsers(@RequestBody MstUsers mstUsers) {
          try {
               MstUsers createUsers = userService.createUsers(mstUsers);
               return ResponseUtil.successResponse(createUsers);    
          } catch (BadRequest e) {
               return ResponseUtil.badRequest(e, HttpStatus.BAD_REQUEST.value());
          } catch (RuntimeException e) {
               return ResponseUtil.errorResponse("Internal Server Error" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
          }
     }

     /**
      * This API for getting the information about the individual user.
      * @param id
      * @return
      */
     @RequestMapping(value = "details/{id}")
     public Optional<MstUsers> getUserByID(@PathVariable("id") Integer id) {
          return userService.getUserByID(id);
     }

     @PostMapping(value = "update/{id}")
     public ResponseEntity<Map<String, Object>> updateUser(@PathVariable("id") Integer id,@RequestBody MstUsers mstUsers) {
          try {
               Optional<MstUsers> updatedUser = userService.updateUser(id, mstUsers);

               if(updatedUser.isEmpty()) {
                    return ResponseUtil.errorResponse("User Not found", HttpStatus.NOT_FOUND.value());
               }
               MstUsers response = updatedUser.get();
               return ResponseUtil.successResponse(response);
          } catch (Exception e) {
               return ResponseUtil.errorResponse("Internal Server Error " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
          }
     }

     /**
      * This API for deleting a particular user.
      * @param id
      * @return
      */
     @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
     public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Integer id) {
          Optional<MstUsers> deletedUser = userService.deletedUser(id);

          if(deletedUser.isEmpty()) {
               return ResponseUtil.errorResponse("Failed to delete", HttpStatus.NOT_FOUND.value());
          }

          MstUsers response = deletedUser.get();
          return ResponseUtil.successResponse(response);
     }

     /**
      * This API is used for getting all the data related to the user so it can be set in the frontend.
      * @param request
      * @return
      */
     @RequestMapping(value = "me", method = RequestMethod.GET)
     public ResponseEntity<Map<String, Object>> me(HttpServletRequest request) {
          Map<String, Object> id = userService.me(request);

          if(id == null) {
               return ResponseUtil.errorResponse("No user found", HttpStatus.NOT_FOUND.value());
          }

          return ResponseUtil.successResponse(id);
     }

     /**
      * This API is for sending the email of OTP to user for the resetting the password.
      * @param email
      * @return
      */
     @PostMapping(value = "otp")
     public ResponseEntity<Map<String, Object>> sendEmailForOtp(@RequestParam String email) {
          try {
               Map<String, Object> data = userService.sendEmailForOtp(email);
               if(data == null) {
                    return ResponseUtil.errorResponse("No user found", HttpStatus.NOT_FOUND.value());
               }
               return ResponseUtil.successResponse(data);
          } catch (Exception e) {
               return ResponseUtil.errorResponse("Internal Server Error" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
          }
     }

     /**
      * This API is for verifying the OTP which user enters.
      * @param email
      * @param otp
      * @return
      */
     @PostMapping(value = "otp-verification")
     public ResponseEntity<Map<String, Object>> otpVerification(@RequestParam String email, Integer otp) {
          try {
               Integer data = userService.otpVerification(email, otp);
               if(data == 1) {
                    return ResponseUtil.errorResponse("User not found", HttpStatus.NOT_FOUND.value());
               }
               else if(data == OTP_ERROR_RES) {
                    return ResponseUtil.errorResponse("Failed to verify the OTP", HttpStatus.NOT_FOUND.value());
               }
               else {
                    return ResponseUtil.successResponse(data);
               }
          } catch (Exception e) {
               return ResponseUtil.errorResponse("Internal Server Error " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
          }
     }

     /**
      * This API is to reset password.
      * @param email
      * @param password
      * @return
      */
     @PostMapping(value = "reset-password")
     public ResponseEntity<Map<String, Object>> resetPassword(@RequestParam String email, String password) {
          try {
               Integer data = userService.resetPassword(email, password);
               if(data == 1) {
                    return ResponseUtil.errorResponse("User not found", HttpStatus.NOT_FOUND.value());
               } else {
                    return ResponseUtil.successResponse(data);
               }
          } catch (Exception e) {
               return ResponseUtil.errorResponse("Internal Server Error " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
          }
     }
}
