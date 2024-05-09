package furscan.furscan.Entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class MstUsers {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "user_id", updatable = false, nullable = false )
     private Integer user_id;
     private String first_name;
     private String last_name;
     private String email;
     private String password;
     private String phone_no;
     private String profile_image_text;
     private Integer is_doctor;
     private Integer is_active;
     @Temporal(TemporalType.TIMESTAMP)
     private LocalDateTime created_at;
     @Temporal(TemporalType.TIMESTAMP)
     private LocalDateTime updated_at;
     @Temporal(TemporalType.TIMESTAMP)
     private LocalDateTime deleted_at;
     private Integer otp;

     public Integer getOtp() {
          return this.otp;
     }

     public void setOtp(Integer otp) {
          this.otp = otp;
     }

     
     public MstUsers() {

     }
     
     public MstUsers(Integer user_id, String first_name, String last_name,String email, String password, String phone_no, String profile_image_text, Integer is_doctor, Integer is_active, LocalDateTime created_at, LocalDateTime deleted_at, LocalDateTime updated_at, Integer otp) {
          this.user_id = user_id;
          this.first_name = first_name;
          this.email = email;
          this.password = password;
          this.last_name = last_name;
          this.phone_no = phone_no;
          this.profile_image_text = profile_image_text;
          this.is_doctor = is_doctor;
          this.is_active = is_active;
          this.created_at = created_at;
          this.updated_at = updated_at;
          this.deleted_at = deleted_at;
          this.otp = otp;
     }

     public Integer getUser_id() {
          return this.user_id;
     }

     public void setUser_id(Integer user_id) {
          this.user_id = user_id;
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

     public LocalDateTime getCreated_at() {
          return this.created_at;
     }

     public void setCreated_at(LocalDateTime created_at) {
          this.created_at = created_at;
     }

     public LocalDateTime getUpdated_at() {
          return this.updated_at;
     }

     public void setUpdated_at(LocalDateTime updated_at) {
          this.updated_at = updated_at;
     }

     public LocalDateTime getDeleted_at() {
          return this.deleted_at;
     }

     public void setDeleted_at(LocalDateTime deleted_at) {
          this.deleted_at = deleted_at;
     }

     @PrePersist
     public void onCreate() {
          created_at = LocalDateTime.now();
          updated_at = LocalDateTime.now();
     }

     @PreUpdate
     public void onUpdate() {
          updated_at = LocalDateTime.now();
     }

}
