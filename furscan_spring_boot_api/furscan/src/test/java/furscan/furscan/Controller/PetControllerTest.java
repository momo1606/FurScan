package furscan.furscan.Controller;


import furscan.furscan.Entity.PetReport;
import furscan.furscan.Services.PetService;
import furscan.furscan.Utils.ResponseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PetControllerTest {

    @Mock
    private PetService petService;

    @Mock
    private PetReport petReport;

    @Mock
    private List<PetReport> petData;

    @Mock
    MultipartFile file;

    @Mock
    Optional<PetReport> optionalPetReport;

    @Mock
    ResponseEntity<Map<String, Object>> expectedSuccessResponse;

    @Mock
    ResponseEntity<Map<String, Object>> expectedErrorResponse;

    private ResponseEntity<Map<String, Object>> actualResponse;
    Integer id;

    @InjectMocks
    private PetController petController;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        id = 1;
    }

    @Test
    public void index_PetDataNotNull_ReturnSuccess(){
        when(petService.index(id)).thenReturn(petData);
        expectedSuccessResponse = ResponseUtil.successResponse(petData);
        actualResponse = petController.index(id);
        assertEquals(expectedSuccessResponse, actualResponse);
    }

    @Test
    public void index_PetDataNull_ReturnError(){
        when(petService.index(id)).thenReturn(null);
        expectedErrorResponse = ResponseUtil.errorResponse("Not Pet history available", HttpStatus.NOT_FOUND.value());
        actualResponse = petController.index(id);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    public void create_ReturnSuccess(){
        when(petService.create("petReport", file)).thenReturn(petReport);
        expectedSuccessResponse = ResponseUtil.successResponse(petReport);
        actualResponse = petController.create("petReport", file);
        assertEquals(expectedSuccessResponse, actualResponse);
    }

    @Test
    public void create_ReturnError(){
        //Mocking the Exception
        Exception e = mock(Exception.class);

        when(petService.create("petReport", file)).thenThrow(new RuntimeException());
        expectedErrorResponse = ResponseUtil.errorResponse("Failed to save" + e.getMessage(), HttpStatus.NOT_FOUND.value());
        actualResponse = petController.create("petReport", file);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    public void details_petReportNotNull_ReturnSuccess(){
        when(petService.details(id)).thenReturn(optionalPetReport);
        expectedSuccessResponse = ResponseUtil.successResponse(optionalPetReport);
        actualResponse = petController.details(id);
        assertEquals(expectedSuccessResponse, actualResponse);
    }

    @Test
    public void details_petReportNull_ReturnPetNotFound(){
        when(petService.details(id)).thenReturn(null);
        expectedErrorResponse = ResponseUtil.errorResponse("Pet details not found", HttpStatus.NOT_FOUND.value());
        actualResponse = petController.details(id);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    public void details_ReturnError(){
        //Mocking the Exception
        Exception e = mock(Exception.class);
        String msg = "Internal Server Error" + e.getMessage();

        when(petService.details(id)).thenThrow(new RuntimeException());
        expectedErrorResponse = ResponseUtil.errorResponse(msg, HttpStatus.INTERNAL_SERVER_ERROR.value());
        actualResponse = petController.details(id);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    public void sendToDoctor_isTrue_ReturnSuccess(){
        when(petService.sendToDoctor(id)).thenReturn(true);
        expectedSuccessResponse = ResponseUtil.successResponse(true);
        actualResponse = petController.sendToDoctor(id);
        assertEquals(expectedSuccessResponse, actualResponse);
    }

    @Test
    public void sendToDoctor_isFalse_ReturnReportNotSent(){
        when(petService.sendToDoctor(id)).thenReturn(false);
        expectedErrorResponse = ResponseUtil.errorResponse("Couldn't send the report", HttpStatus.NOT_FOUND.value());
        actualResponse = petController.sendToDoctor(id);
        assertEquals(expectedErrorResponse, actualResponse);
    }

    @Test
    public void sendToDoctor_ReturnError(){
        //Mocking the Exception
        Exception e = mock(Exception.class);
        String msg = "Internal Server Error " + e.getMessage();

        when(petService.sendToDoctor(id)).thenThrow(new RuntimeException());
        expectedErrorResponse = ResponseUtil.errorResponse(msg, HttpStatus.INTERNAL_SERVER_ERROR.value());
        actualResponse = petController.sendToDoctor(id);
        assertEquals(expectedErrorResponse, actualResponse);
    }

}
