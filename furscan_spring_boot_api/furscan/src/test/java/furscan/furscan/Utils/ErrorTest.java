package furscan.furscan.Utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ErrorTest {

    @Test
    void getTargetTest() {
        String target = "example";
        String message = "Error message";
        Error error = new Error(target, message);
        String resultTarget = error.getTarget();
        assertEquals(target, resultTarget);
    }

    @Test
    void setTargetTest() {
        String target = "example";
        String message = "Error message";
        Error error = new Error(target,message);
        error.setTarget(target);
        assertEquals(target, error.getTarget());
    }

    @Test
    void getMessageTest() {
        String target = "test";
        String message = "Error message";
        Error error = new Error(target, message);
        String resultMessage = error.getMessage();
        assertEquals(message, resultMessage);
    }

    @Test
    void setMessageTest() {
        String target = "test";
        String message = "Error message";
        Error error = new Error(target,message);
        error.setMessage(message);
        assertEquals(message, error.getMessage());
    }

    @Test
    void constructorTest() {
        String target = "test";
        String message = "Error message";
        Error error = new Error(target, message);
        assertEquals(target, error.getTarget());
        assertEquals(message, error.getMessage());
    }
}
