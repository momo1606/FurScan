package furscan.furscan.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static furscan.furscan.Constants.PET_AGE;
import static furscan.furscan.Constants.USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetReportTest {
    @InjectMocks
    private PetReport petReport;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        //Initializing PetReport
        petReport = new PetReport(1, USER_ID, 0, "Pixie", PET_AGE, "Daschund", "testImageText", 1, "Pet remarks", LocalDateTime.now(), LocalDateTime.now(), "fungal");
    }

    @Test
    public void test_getPet_report_id(){
        assertEquals(1, petReport.getPet_report_id());
    }

    @Test
    public void test_getDisease(){
        assertEquals("fungal", petReport.getDisease());
    }

    @Test
    public void test_getPet_name(){
        assertEquals("Pixie", petReport.getPet_name());
    }

    @Test
    public void test_getAge(){
        assertEquals(PET_AGE, petReport.getAge());
    }

    @Test
    public void test_getBreed(){
        assertEquals("Daschund", petReport.getBreed());
    }

    @Test
    public void test_getStatus(){
        assertEquals(1, petReport.getStatus());
    }

    @Test
    public void get_getis_Send(){
        assertEquals(0, petReport.getis_Send());
    }
}
