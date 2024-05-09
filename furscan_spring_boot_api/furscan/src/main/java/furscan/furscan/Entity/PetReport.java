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
public class PetReport {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "pet_report_id", updatable = false, nullable = false)
     private Integer pet_report_id;

     public Integer getPet_report_id() {
          return this.pet_report_id;
     }

     public void setPet_report_id(Integer pet_report_id) {
          this.pet_report_id = pet_report_id;
     }
     private String pet_name;
     private Integer age;
     private String breed;
     private String image_text;
     private Integer status;
     private String remarks;
     private Integer user_id;
     private String report_text;
     private Integer is_send;
     private String disease;

     public String getDisease() {
          return this.disease;
     }

     public void setDisease(String disease) {
          this.disease = disease;
     }

     public String getReport_text() {
          return this.report_text;
     }

     public void setReport_text(String report_text) {
          this.report_text = report_text;
     }

     public Integer getUser_id() {
          return this.user_id;
     }

     public void setUser_id(Integer user_id) {
          this.user_id = user_id;
     }


     public String getPet_name() {
          return this.pet_name;
     }

     public void setPet_name(String pet_name) {
          this.pet_name = pet_name;
     }

     public Integer getAge() {
          return this.age;
     }

     public void setAge(Integer age) {
          this.age = age;
     }

     public String getBreed() {
          return this.breed;
     }

     public void setBreed(String breed) {
          this.breed = breed;
     }

     public String getImage_text() {
          return this.image_text;
     }

     public void setImage_text(String image_text) {
          this.image_text = image_text;
     }

     public Integer getStatus() {
          return this.status;
     }

     public void setStatus(Integer status) {
          this.status = status;
     }

     public String getRemarks() {
          return this.remarks;
     }

     public void setRemarks(String remarks) {
          this.remarks = remarks;
     }

     @Temporal(TemporalType.TIMESTAMP)
     private LocalDateTime created_at;

     public LocalDateTime getCreated_at() {
          return this.created_at;
     }

     public void setCreated_at(LocalDateTime created_at) {
          this.created_at = created_at;
     }

     @Temporal(TemporalType.TIMESTAMP)
     private LocalDateTime updated_at;

     public LocalDateTime getUpdated_at() {
          return this.updated_at;
     }

     public void setUpdated_at(LocalDateTime updated_at) {
          this.updated_at = updated_at;
     }

     public Integer getis_Send() {
          return this.is_send;
     }

     public void setis_Send(Integer is_send) {
          this.is_send = is_send;
     }


     public PetReport() {

     }

     public PetReport(Integer pet_report_id, Integer user_id, Integer is_send, String pet_name, Integer age, String breed, String image_text, Integer status, String remarks, LocalDateTime created_at, LocalDateTime updated_at, String disease) {
          this.pet_report_id = pet_report_id;
          this.user_id = user_id;
          this.is_send = is_send;
          this.pet_name = pet_name;
          this.age = age;
          this.breed = breed;
          this.image_text = image_text;
          this.status = status;
          this.remarks = remarks;
          this.updated_at = updated_at;
          this.created_at = created_at;
          this.disease = disease;
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
