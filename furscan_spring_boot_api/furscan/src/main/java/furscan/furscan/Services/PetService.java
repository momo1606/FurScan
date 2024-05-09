package furscan.furscan.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Entity.PetReport;
import furscan.furscan.Repository.PetReportRepository;
import furscan.furscan.Repository.UserRepository;
import furscan.furscan.Utils.EmailSenderUtil;

@Service
public class PetService {
     @Autowired
     private PetReportRepository petReportRepository;

     @Autowired
     private ObjectMapper objectMapper;

     @Autowired
     private S3FileService s3FileService;

     @Autowired
     private EmailSenderUtil senderUtil;

     @Autowired
     private UserRepository userRepository;

     /**
      * Get the list of the Pets.
      * @param id
      * @return
      */
     public List<PetReport> index(Integer id) {
          List<PetReport> petData = petReportRepository.index(id);
          if(petData.isEmpty()) {
               return null;
          }

          for(PetReport petReport : petData) {
               String key = petReport.getImage_text();
               String url = "https://furscan.s3.amazonaws.com/" + key;
               petReport.setImage_text(url);

               String reportKey = petReport.getReport_text();
               String reportUrl = "https://furscan.s3.amazonaws.com/" + reportKey;
               petReport.setReport_text(reportUrl);
          }
          return petData;
     }

     /**
      * Create the pet health request
      * @param petReport
      * @param file
      * @return
      */
     public PetReport create(String petReport, MultipartFile file) {
          try {
               String fileString = s3FileService.uploadFile(file);
               PetReport pet = null;
               pet = objectMapper.readValue(petReport, PetReport.class);
               String key = "dog_images/" + fileString;
               pet.setImage_text(key);
               pet = petReportRepository.save(pet);
               
               String pet_report = predictionRequest(pet.getPet_report_id(), fileString);
               System.out.println(pet_report);
               Integer user_id = pet.getUser_id();

               MstUsers mstUsers = userRepository.findById(user_id).orElse(null);
               String subject = "Pet Report is received";
               String body= "Hello, " + mstUsers.getFirst_name() +"!\n\n"
               + "Your pet report is ready please find the attachment below. "
               + "We hope you're having a great day!\n\n"
               + "Best regards,\n"
               + "FurScan Team";
               String to = mstUsers.getEmail();

               JsonNode root = objectMapper.readTree(pet_report);
               String attachment = "https://furscan.s3.amazonaws.com/" + root.get("report_object_key").asText();
               triggerEmail(to, body, subject, attachment);
               if(pet_report == null) {
                    return null;
               }
               return pet;
          } catch (JsonProcessingException e) {
               return null;
          }
     }

     /**
      * Get the details for the particular Pet.
      * @param id
      * @return
      */
     public Optional<PetReport> details(Integer id) {
          Optional<PetReport> petReport = petReportRepository.findById(id);

          if(petReport.isEmpty()) {
               return null;
          }
          return petReport;
     }

     /**
      * Request the ML function to generate the Pet Report.
      * @param pet_report_id
      * @param objectKey
      * @return
      */
     private String predictionRequest(Integer pet_report_id, String objectKey) {
          String predictionUrl = "http://52.23.167.187/predict/" + pet_report_id + "/" + objectKey;
          System.out.println(predictionUrl);

          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.APPLICATION_JSON);
          HttpEntity<String> request = new HttpEntity<>(headers);
          RestTemplate restTemplate = new RestTemplate();

          String response = restTemplate.postForObject(predictionUrl, request,String.class);

          return response;
     }

     /**
      * Send the report to the doctor.
      * @param id
      * @return
      */
     @Transactional
     public boolean sendToDoctor(Integer id) {
          petReportRepository.sendToDoctor(id);
          return true;
     }

     /**
      * Helper function to trigger the email after report is generated.
      * @param to
      * @param body
      * @param subject
      * @param attachment
      */
     public void triggerEmail(String to, String body, String subject, String attachment) {
          senderUtil.sendEmailWithAttachment(to, body, subject, attachment);
     }
}
