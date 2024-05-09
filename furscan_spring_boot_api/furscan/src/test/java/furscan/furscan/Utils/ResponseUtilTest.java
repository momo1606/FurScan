package furscan.furscan.Utils;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static furscan.furscan.Constants.BAD_REQ_RESPONSE_CODE;
import static furscan.furscan.Constants.ERROR_RESPONSE_CODE;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResponseUtilTest {

    @Test
    void successResponseTest() {
        String testData = "Test data";
        ResponseEntity<Map<String, Object>> responseEntity = ResponseUtil.successResponse(testData);
        Map<String, Object> responseBody = responseEntity.getBody();
        assertEquals("Success", responseBody.get("status"));
    }

    @Test
    void errorResponseTest() {
        String errorMessage = "Test error message";
        int statusCode = ERROR_RESPONSE_CODE;
        ResponseEntity<Map<String, Object>> responseEntity = ResponseUtil.errorResponse(errorMessage, statusCode);
        Map<String, Object> responseBody = responseEntity.getBody();
        assertEquals(errorMessage, responseBody.get("message"));
    }

    @Test
    void badRequestTest() {
        Error error = new Error("target", "error");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        BadRequest badRequest = new BadRequest("Bad request", errors);
        int statusCode = BAD_REQ_RESPONSE_CODE;
        ResponseEntity<Map<String, Object>> responseEntity = ResponseUtil.badRequest(badRequest, statusCode);
        Map<String, Object> responseBody = responseEntity.getBody();
        assertEquals(statusCode, responseBody.get("status"));
    }
}
