package furscan.furscan.Controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import furscan.furscan.Entity.PetReport;
import furscan.furscan.Services.VetService;
import furscan.furscan.Utils.ResponseUtil;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VetControllerTest {

    @Mock
    private PetReport petReport;
    @Mock
    private VetService vetService;
    @Mock
    private ResponseUtil responseUtil;
    @Mock
    private Map<String, String> remarks;
    @Mock
    private PetReport mockPetReport;
    @InjectMocks
    private VetController vetController;

    Integer id;
    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        id = 1;
        //petReport = new PetReport(1, 10,1, "Pixie", 2, "Daschund", "testImageText", 1, "Pet remarks", LocalDateTime.now(), LocalDateTime.now(),"disease");
    }


    @Test
    public void testIndex_Success() {
        List<PetReport> mockPetReports = Collections.singletonList(new PetReport());
        when(vetService.index()).thenReturn(mockPetReports);
        ResponseEntity<Map<String, Object>> responseEntity = vetController.index();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testIndex_noData() {
        List<PetReport> mockPetReports = null;
        when(vetService.index()).thenReturn(mockPetReports);
        ResponseEntity<Map<String, Object>> responseEntity = vetController.index();
        assertEquals(mockPetReports, responseEntity.getBody().get("data"));
    }
    @Test
    public void testDetails_success() {
        Optional<PetReport> mockOptional = Optional.of(mockPetReport);
        when(vetService.details(any(Integer.class))).thenReturn(mockOptional);
        ResponseEntity<Map<String, Object>> responseEntity = vetController.details(1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testDetails_nullData() {
        Optional<PetReport> mockOptional = null;
        when(vetService.details(any(Integer.class))).thenReturn(mockOptional);
        ResponseEntity<Map<String, Object>> responseEntity = vetController.details(1);
        assertEquals(mockOptional, responseEntity.getBody().get("data"));
    }

    @Test
    public void testDetails_error() {
        Optional<PetReport> mockOptional = Optional.of(mockPetReport);
        when(vetService.details(any(Integer.class))).thenThrow(new RuntimeException());
        ResponseEntity<Map<String, Object>> responseEntity = vetController.details(1);
        assertEquals("Internal Server Errornull", responseEntity.getBody().get("message"));
    }

    @Test
    public void testAddRemarks_success() {
        Map<String, String> remarks = Map.of("id", "1", "remarks", "Test remarks");
        when(vetService.updateReport(any(Integer.class), any(String.class))).thenReturn(petReport);
        ResponseEntity<Map<String, Object>> responseEntity = vetController.addRemakrs(remarks);
        assertEquals(petReport, responseEntity.getBody().get("data"));
    }

    @Test
    public void testAddRemarks_noData() {
        Map<String, String> remarks = Map.of("id", "1", "remarks", "Test remarks");
        when(vetService.updateReport(any(Integer.class), any(String.class))).thenReturn(null);
        ResponseEntity<Map<String, Object>> responseEntity = vetController.addRemakrs(remarks);
        assertNull(responseEntity.getBody().get("data"));
    }

    @Test
    public void testAddRemarks_returnException(){
        RuntimeException e = mock(RuntimeException.class);
        when(vetService.updateReport(any(Integer.class), any(String.class))).thenThrow(e);
        ResponseEntity<Map<String, Object>> responseEntity = vetController.addRemakrs(remarks);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}
