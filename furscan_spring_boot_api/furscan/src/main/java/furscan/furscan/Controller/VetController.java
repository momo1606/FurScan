package furscan.furscan.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import furscan.furscan.Entity.PetReport;
import furscan.furscan.Services.VetService;
import furscan.furscan.Utils.ResponseUtil;

@Controller
@RequestMapping("/vet")
public class VetController {

    @Autowired
    private VetService vetService;

    /**
     * This API is to get the list of the Pet request.
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> index() {
        List<PetReport> data = vetService.index();

        if(data == null) {
            return ResponseUtil.errorResponse("Not Pet history available", HttpStatus.NOT_FOUND.value());
        }
        return ResponseUtil.successResponse(data);
    }

    /**
     * This API is for getting the details of the individual Pet.
     * @param id
     * @return
     */
    @GetMapping(value = "/details/{id}")
    public ResponseEntity<Map<String, Object>> details(@PathVariable("id") Integer id) {
        try {
            Optional<PetReport> data = vetService.details(id);
            if(data == null) {
                return ResponseUtil.errorResponse("Pet details not found", HttpStatus.NOT_FOUND.value());
            }
            return ResponseUtil.successResponse(data);
        } catch (RuntimeException e) {
            return ResponseUtil.errorResponse("Internal Server Error" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * This API is to for the doctor to add the remarks for the Pet Report. 
     * @param remarks
     * @return
     */
    @PutMapping (value = "/add-remarks")
    public ResponseEntity<Map<String, Object>> addRemakrs(@RequestBody Map<String,String> remarks){
        try {
            Integer id = Integer.valueOf(remarks.get("id"));
            String remark = remarks.get("remarks");
            PetReport data = vetService.updateReport(id, remark);
            if(data == null) {
                return ResponseUtil.errorResponse("Couldn't update the report", HttpStatus.NOT_FOUND.value());
            }
            return ResponseUtil.successResponse(data);
        } catch (Exception e) {
            return ResponseUtil.errorResponse("Internal Server Error " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}