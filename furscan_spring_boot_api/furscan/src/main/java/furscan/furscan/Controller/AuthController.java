package furscan.furscan.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import furscan.furscan.DTO.LoginRequestDto;
import furscan.furscan.DTO.SignupRequestDto;
import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Services.AuthService;
import furscan.furscan.Utils.ResponseUtil;
import lombok.RequiredArgsConstructor;

import static furscan.furscan.Constants.INTERNAL_SERVER_ERROR_VALUE;

@RequiredArgsConstructor
@CrossOrigin
@Controller
public class AuthController {
     @Autowired
     private AuthService authService;


     /**
      * This particular function performs the actions of signing up the User or Doctor.
      * @param signupRequestDto
      * @return
      */
     @PostMapping("/signup")
     public ResponseEntity<Map<String, Object>> signUp(@RequestBody SignupRequestDto signupRequestDto) {
          try {
               MstUsers auth = authService.signUp(signupRequestDto);
               return ResponseUtil.successResponse(auth);
          } catch (Exception e) {
               return ResponseUtil.errorResponse("Internal Server Error", INTERNAL_SERVER_ERROR_VALUE);
          }
     }

     /**
      * This function is for login of the user.
      * @param loginRequestDto
      * @return
      */
     @PostMapping("/login")
     public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto loginRequestDto) {
          try {
               Map<String, Object> auth = authService.login(loginRequestDto);

               if(auth == null) {
                    return ResponseUtil.errorResponse("Login failed", HttpStatus.UNAUTHORIZED.value());
               }    
               return ResponseUtil.successResponse(auth);
          } catch (Exception e) {
               return ResponseUtil.errorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
          }
     }
}
