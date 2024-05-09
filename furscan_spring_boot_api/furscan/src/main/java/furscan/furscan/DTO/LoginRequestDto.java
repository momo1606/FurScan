package furscan.furscan.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDto {
     private String email;
     private String password;
     private String first_name;
     private String last_name;

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

}
