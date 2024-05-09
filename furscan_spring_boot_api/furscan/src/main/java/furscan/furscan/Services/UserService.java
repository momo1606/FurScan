package furscan.furscan.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Repository.UserRepository;
import furscan.furscan.Utils.BadRequest;
import furscan.furscan.Utils.EmailSenderUtil;
import furscan.furscan.Utils.Error;
import furscan.furscan.Utils.JwtUtil;
import furscan.furscan.Validator.UserValidation;
import jakarta.servlet.http.HttpServletRequest;

import static furscan.furscan.Constants.*;

@Service
public class UserService {
     @Autowired
     private UserRepository userRepository;

     @Autowired
     private UserValidation userValidation;

     @Autowired
     private JwtUtil jwtUtil;

     @Autowired
     private BCryptPasswordEncoder bCryptPasswordEncoder;

     @Autowired
     private EmailSenderUtil senderUtil;

     /**
      * Get all the user list
      * @return
      */
     public List<MstUsers> getUser() {

          List<MstUsers> mst_users = new ArrayList<>();

          userRepository.findAll()
               .forEach(users -> mst_users.add(users));
          if(mst_users.size() <= 0) {
               return null;
          }
          return mst_users;
     }

     /**
      * Create the users.
      * @param mstUsers
      * @return
      */
     public MstUsers createUsers(MstUsers mstUsers) {
          List<Error> errors = userValidation.createUpdateRequest(mstUsers);
          String encodePassword = bCryptPasswordEncoder.encode(mstUsers.getPassword());
          mstUsers.setPassword(encodePassword);
            if(errors.size() > 0) {
               throw new BadRequest("Bad Request",errors);
          }
          return userRepository.save(mstUsers);
     }

     /**
      * Get the user by ID.
      * @param id
      * @return
      */
     public Optional<MstUsers> getUserByID(Integer id) {
          return userRepository.findById(id);
     }

     /**
      * Update the users.
      * @param id
      * @param mstUsers
      * @return
      */
     public Optional<MstUsers> updateUser(Integer id,MstUsers mstUsers) {
          Optional<MstUsers> isUserPresent = userRepository.findById(id);
          if(isUserPresent == null) {
               return null;
          }
          System.out.println(mstUsers.getPassword());
          isUserPresent.ifPresent(user -> {
               user.setFirst_name(mstUsers.getFirst_name());
               user.setLast_name(mstUsers.getLast_name());
               user.setEmail(mstUsers.getEmail());
               user.setPhone_no(mstUsers.getPhone_no());
               userRepository.save(user);
          });
          return isUserPresent;
     }

     /**
      * Delete the individual user.
      * @param id
      * @return
      */
     public Optional <MstUsers> deletedUser(Integer id) {
          Optional<MstUsers> isUserPresent = userRepository.findById(id);

          isUserPresent.ifPresent(user ->{
               userRepository.deleteById(id);
          });
          return isUserPresent;
     }

     /**
      * Get he details of the user. 
      * @param request
      * @return
      */
     public Map<String, Object> me(HttpServletRequest request){
          MstUsers user = jwtUtil.getUser(request);
          Integer id = user.getUser_id();
          List<Map<String, Object>> userPetDetails = userRepository.petList(id);
          Map<String, Object> data = new HashMap<>();
          data.put("user", user);
          data.put("petReport", userPetDetails);
          return data;
     }

     public Map<String, Object> sendEmailForOtp(String email) {
          Map<String, Object> data = new HashMap<>();
          Random random = new Random();
         int otp = random.nextInt(RANDOM_MIN_VALUE) + RANDOM_MAX_VALUE;
          String subject = "OTP for reset password ❕";
               String body= "Hello," +"\n\n"
               + "OTP for reset password: " + otp + "\n\n"
               + "Best regards,\n"
               + "FurScan Team";
          MstUsers user = userRepository.findOneByEmail(email);
          if( user == null) {
               data.put("data", null);
               return data;
          }
          user.setOtp(otp);
          userRepository.save(user);
          triggerEmail(email, body, subject);
          data.put("email", email);
          return data;
     }

     public Integer otpVerification(String email, Integer otp) {
          MstUsers user = userRepository.findOneByEmail(email);
          if(user == null) {
               return 1;
          }
          Integer getOtp = user.getOtp();
          if(getOtp.equals(otp)) {
               return OTP_SUCCESS_RES;
          }
          return OTP_ERROR_RES;
     }

     public Integer resetPassword(String email, String password) {
          MstUsers user = userRepository.findOneByEmail(email);
          if(user == null) {
               return 1;
          } else {
               String encodedPassword = bCryptPasswordEncoder.encode(password);
               user.setPassword(encodedPassword);
               userRepository.save(user);
               return RESET_PWD_SUCCESS;
          }
     }

     public void triggerEmail(String to, String body, String subject) {
          senderUtil.sendEmailWithoutAttachment(to, body, subject);
     }
}
