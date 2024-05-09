package furscan.furscan.Utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BadRequestTest {

    private List<Error> errors;

    @BeforeEach
    public void inti(){
        errors = Arrays.asList(new Error("field1", "error1"), new Error("field2", "error2"));
    }

    @Test
    public void getErrorTest() {

        BadRequest badRequest = new BadRequest("Bad request", errors);
        List<Error> resultErrors = badRequest.getError();
        assertEquals(errors, resultErrors);
    }

    @Test
    public void setErrorTest() {
        BadRequest badRequest = new BadRequest("Bad request", null);
        badRequest.setError(errors);
        assertEquals(errors, badRequest.getError());
    }


    @Test
    public void constructorAndGetErrorTest() {
        String message = "Bad request";
        BadRequest badRequest = new BadRequest(message, errors);
        assertEquals(errors, badRequest.getError());
    }

    @Test
    public void constructorWithNullErrorTest() {
        String message = "Bad request";
        BadRequest badRequest = new BadRequest(message, null);
        assertNull(badRequest.getError());
    }
}
