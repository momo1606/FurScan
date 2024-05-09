package furscan.furscan.Utils;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

     /**
      * Generating the successResponse for the response.
      * @param data
      * @return
      */
     public static ResponseEntity<Map<String, Object>> successResponse(Object data) {
          Map<String, Object> response = new HashMap<>();
          response.put("status", "Success");
          response.put("data", data);
          return ResponseEntity.status(HttpStatus.OK).body(response);
     }

     /**
      * Generating the error reponse for the errors.
      * @param message
      * @param i
      * @return
      */
     public static ResponseEntity<Map<String, Object>> errorResponse(String message, int i) {
          Map<String, Object> response = new HashMap<>();
          response.put("status", i);
          response.put("message", message);
          response.put("data", null);
          return ResponseEntity.status(i).body(response);
     }

     public static ResponseEntity<Map<String, Object>> badRequest(BadRequest e, int i) {
          Map<String, Object> response = new HashMap<>();
          response.put("data", null);
          response.put("error",e.getError());
          response.put("status", i);
          return ResponseEntity.status(i).body(response);
     }

}
