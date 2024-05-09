package furscan.furscan.DTO;

import lombok.Builder;

@Builder
public class SignupRequestDto {
     private String first_name;
     private String last_name;
     private String email;
     private String password;
     private String phone_no;
     private String profile_image_text;
     private Integer is_doctor;
     private Integer is_active;

     public SignupRequestDto(String first_name, String last_name, String email, String password, String phone_no, String profile_image_text, Integer is_doctor, Integer is_active) {
          this.first_name = first_name;
          this.last_name = last_name;
          this.email = email;
          this.password = password;
          this.phone_no = phone_no;
          this.profile_image_text = profile_image_text;
          this.is_doctor = is_doctor;
          this.is_active = is_active;
     }

     public String getProfile_image_text() {
          return this.profile_image_text;
     }

     public void setProfile_image_text(String profile_image_text) {
          this.profile_image_text = profile_image_text;
     }

     public Integer getIs_doctor() {
          return this.is_doctor;
     }

     public void setIs_doctor(Integer is_doctor) {
          this.is_doctor = is_doctor;
     }

     public Integer getIs_active() {
          return this.is_active;
     }

     public void setIs_active(Integer is_active) {
          this.is_active = is_active;
     }

     public String getFirst_name() {
          return this.first_name;
     }

     public void setFirst_name(String first_name) {
          this.first_name = first_name;
     }

     public String getLast_name() {
          return this.last_name;
     }

     public void setLast_name(String last_name) {
          this.last_name = last_name;
     }

     public String getEmail() {
          return this.email;
     }

     public void setEmail(String email) {
          this.email = email;
     }

     public String getPassword() {
          return this.password;
     }

     public void setPassword(String password) {
          this.password = password;
     }

     public String getPhone_no() {
          return this.phone_no;
     }

     public void setPhone_no(String phone_no) {
          this.phone_no = phone_no;
     }

}
