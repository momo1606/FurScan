package furscan.furscan.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Entity.PetReport;
import furscan.furscan.Repository.PetReportRepository;
import furscan.furscan.Repository.UserRepository;
import furscan.furscan.Services.PetService;
import furscan.furscan.Services.S3FileService;
import furscan.furscan.Utils.EmailSenderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static furscan.furscan.Constants.PET_AGE;
import static furscan.furscan.Constants.USER_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PetServiceTest {

    @Mock
    private PetReportRepository petReportRepository;

    @Mock
    private EmailSenderUtil senderUtil;

    @Mock
    private List<PetReport> petReportList;

    @Mock
    private PetReport petReport;

    @Mock
    private Optional<PetReport> petReportOptional;

    private Integer id;

    @InjectMocks
    private PetService petService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        //Initializing id
        id = 1;
        Integer user_id = USER_ID;

        //Initializing PetReport
        petReport = new PetReport(id, user_id, 0, "Pixie", PET_AGE, "Daschund", "testImageText", 1, "Pet remarks", LocalDateTime.now(), LocalDateTime.now(), "fungal");

    }

    @Test
    public void index_ReturnPetData(){
        //Initializing List<PetReport>
        petReportList = new ArrayList<>();
        petReportList.add(petReport);

        when(petReportRepository.index(id)).thenReturn(petReportList);

        String key = petReport.getImage_text();
        String url = "https://furscan.s3.amazonaws.com/" + key;
        String reportKey = petReport.getReport_text();
        String reportUrl = "https://furscan.s3.amazonaws.com/" + reportKey;
        List<PetReport> actualResponse = petService.index(id);

        assertEquals(url, actualResponse.get(0).getImage_text());
        assertEquals(reportUrl, actualResponse.get(0).getReport_text());
    }

    @Test
    public void index_ReturnNull(){
        when(petReportRepository.index(id)).thenReturn(petReportList);
        when(petReportList.isEmpty()).thenReturn(true);
        List<PetReport> actualResponse = petService.index(id);

        assertNull(actualResponse);
    }

    @Test
    public void details_ReturnPetReport(){
        when(petReportRepository.findById(id)).thenReturn(petReportOptional);
        assertEquals(petReportOptional, petService.details(id));
    }

    @Test
    public void details_ReturnNull(){
        when(petReportRepository.findById(id)).thenReturn(petReportOptional);
        when(petReportOptional.isEmpty()).thenReturn(true);
        assertEquals(null, petService.details(id));
    }

    @Test
    public void sendToDoctor_ReturnTrue(){
        assertTrue(petService.sendToDoctor(id));
    }

    @Test
    public void triggerEmailTest(){
        String to = "test@gmail.com";
        String body = "Test body";
        String subject = "Test subject";
        String attachment = "attachment.txt";

        petService.triggerEmail(to, body, subject, attachment);

        verify(senderUtil).sendEmailWithAttachment(to, body, subject, attachment);
    }
}
