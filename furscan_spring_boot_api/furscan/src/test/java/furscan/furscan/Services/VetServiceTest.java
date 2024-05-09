package furscan.furscan.Services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


import furscan.furscan.Utils.EmailSenderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import furscan.furscan.Entity.PetReport;
import furscan.furscan.Repository.VetRepository;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;


import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class VetServiceTest {

    @Mock
    private VetRepository vetRepository;
    @Mock
    private PetReport petReport;
    @Mock
    private EmailSenderUtil emailSenderUtil;
    @InjectMocks
    private VetService vetService;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        // petReport = new PetReport(1, 10, "Pixie", 2, "Daschund", "testImageText", 1, "Pet remarks", LocalDateTime.now(), LocalDateTime.now());

    }

    @Test
    public void testIndex_success() {
        List<PetReport> mockPetReports = Collections.singletonList(new PetReport());
        when(vetRepository.is_SendReport()).thenReturn(mockPetReports);
        List<PetReport> result = vetService.index();
        verify(vetRepository).is_SendReport();
        assertEquals(mockPetReports, result);
    }

    @Test
    public void testIndex_emptyData() {
        when(vetRepository.is_SendReport()).thenReturn(Collections.emptyList());
        List<PetReport> result = vetService.index();
        assertNull(result);
    }

    @Test
    public void testDetails_success() {
        PetReport mockPetReport = new PetReport();
        Optional<PetReport> mockOptional = Optional.of(mockPetReport);
        when(vetRepository.findById(any(Integer.class))).thenReturn(mockOptional);
        Optional<PetReport> result = vetService.details(1);
        assertEquals(mockOptional, result);
    }

    @Test
    public void testDetails_notFound() {
        when(vetRepository.findById(anyInt())).thenReturn(Optional.empty());
        Optional<PetReport> result = vetService.details(1);
        verify(vetRepository).findById(anyInt());
        assertNull(result);
    }

    @Test
    public void updateReportTest() {
        PetReport petReport = new PetReport(1, 1,1, "pet", 1, "breed", "image.jpg", 1, "remarks", null, null, null);
        when(vetRepository.findByPet_report_id(anyInt())).thenReturn(petReport);
//        boolean bool = vetService.updateReport(petReport.getRemarks(), "remarks");
        assertEquals("remarks", petReport.getRemarks());
    }

    @Test
    void testTriggerEmail() {
        String to = "test@gmail.com";
        String body = "body";
        String subject = "subject";
        String attachment = "attachment.pdf";
        vetService.triggerEmail(to, body, subject, attachment);
        verify(emailSenderUtil, times(1)).sendEmailWithAttachment(to, body, subject, attachment);
    }
}