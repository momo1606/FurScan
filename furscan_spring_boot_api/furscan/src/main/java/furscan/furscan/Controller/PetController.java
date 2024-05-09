package furscan.furscan.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import furscan.furscan.Entity.PetReport;
import furscan.furscan.Services.PetService;
import furscan.furscan.Utils.ResponseUtil;

@Controller
@RequestMapping("/pet")
public class PetController {
     @Autowired
     private PetService petService;

     /**
      * This function is an listing API to get the list of the Pet History.
      * @param id
      * @return
      */
     @GetMapping("/list")
     public ResponseEntity<Map<String, Object>> index(@RequestParam Integer id) {
          List<PetReport> data = petService.index(id);

          if(data == null) {
               return ResponseUtil.errorResponse("Not Pet history available", HttpStatus.NOT_FOUND.value());
          }
          return ResponseUtil.successResponse(data);
     }

     /**
      * This function is to create a pet health request for the doctor
      * @param petReport
      * @param file
      * @return
      */
     @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
     public ResponseEntity<Map<String, Object>> create(@RequestParam String petReport, @RequestParam(value = "image_text") MultipartFile file) {
          try {
               PetReport data = petService.create(petReport, file);
               return ResponseUtil.successResponse(data);
          } catch (Exception e) {
               return ResponseUtil.errorResponse("Failed to save" + e.getMessage(), HttpStatus.NOT_FOUND.value());
          }
     }

     /**
      * This function gets the details of the particular pet.
      * @param id
      * @return
      */
     @GetMapping(value = "/details/{id}")
     public ResponseEntity<Map<String, Object>> details(@PathVariable("id") Integer id) {
          try {
               Optional<PetReport> data = petService.details(id);
               if(data == null) {
                    return ResponseUtil.errorResponse("Pet details not found", HttpStatus.NOT_FOUND.value());
               }
               return ResponseUtil.successResponse(data);
          } catch (Exception e) {
               return ResponseUtil.errorResponse("Internal Server Error" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
          }
     }

     /**
      * This API for sending the pet health status to the doctor.
      * @param id
      * @return
      */
     @PostMapping(value = "/send-to-doctor")
     public ResponseEntity<Map<String, Object>> sendToDoctor(@RequestParam Integer id) {
          try {
               boolean data = petService.sendToDoctor(id);
               if(data == false) {
                    return ResponseUtil.errorResponse("Couldn't send the report", HttpStatus.NOT_FOUND.value());
               }
               return ResponseUtil.successResponse(data);
          } catch (Exception e) {
               return ResponseUtil.errorResponse("Internal Server Error " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
          }
     }

}
