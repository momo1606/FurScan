package furscan.furscan.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import furscan.furscan.DTO.LoginRequestDto;
import furscan.furscan.DTO.SignupRequestDto;
import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Repository.AuthRepository;
import furscan.furscan.Utils.JwtUtil;

@Service
public class AuthService {

     @Autowired
     private AuthRepository authRepository;

     @Autowired
     private JwtUtil jwtUtil;

     @Autowired
     private AuthenticationManager authenticationManager;

     /**
      * Signup for the user.
      * @param signupRequestDto
      * @return
      */
     public MstUsers signUp(SignupRequestDto signupRequestDto) {
          MstUsers mst_users = new MstUsers();
          mst_users.setFirst_name(signupRequestDto.getFirst_name());
          mst_users.setLast_name(signupRequestDto.getLast_name());
          mst_users.setEmail(signupRequestDto.getEmail());
          mst_users.setPassword(signupRequestDto.getPassword());
          mst_users.setPhone_no(signupRequestDto.getPhone_no());
          mst_users.setProfile_image_text(signupRequestDto.getProfile_image_text());
          mst_users.setIs_active(signupRequestDto.getIs_active());
          mst_users.setIs_doctor(signupRequestDto.getIs_doctor());

          mst_users = authRepository.save(mst_users);
          return mst_users;
     }

     /**
      * Login for the user.
      * @param loginRequestDto
      * @return
      */
     public Map<String, Object> login(@RequestBody LoginRequestDto loginRequestDto) {
         MstUsers user = authRepository.findOneByEmail(loginRequestDto.getEmail());
         String email = loginRequestDto.getEmail();
         String password = loginRequestDto.getPassword();
          UsernamePasswordAuthenticationToken authToken =  new UsernamePasswordAuthenticationToken(email, password);
          Authentication authentication = authenticationManager.authenticate(authToken);
          String token = "";
          if(authentication.isAuthenticated()) {
               token = jwtUtil.generateToken(user);
               Map<String, Object> data = new HashMap<>();
               data.put("accessToken", token); 
               data.put("user", user);
               return data;
          } else {
               return null;
          }
     }
}
