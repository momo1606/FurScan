package furscan.furscan.Services;

import com.amazonaws.services.s3.AmazonS3;
import org.apache.hadoop.shaded.org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class S3FileServiceTest {

    @Mock
    private AmazonS3 amazonS3;

    @InjectMocks
    private S3FileService s3FileService;

    @BeforeEach
    void init() {
        s3FileService = new S3FileService();
        amazonS3 = mock(AmazonS3.class);
        reset(amazonS3);
    }

    @Test
    void uploadFileTest() throws IOException {
        MockMultipartFile mockMultipartFile = createMockMultipartFile("testFile.txt", "Hello, World!");
        doReturn(null).when(amazonS3).putObject(anyString(), anyString(), any(File.class));
        s3FileService.setAmazonS3(amazonS3);
        String result = s3FileService.uploadFile(mockMultipartFile);
        assertNotNull(result);
        verify(amazonS3, times(1)).putObject(eq(null), any(String.class), any(File.class));
    }

    private MockMultipartFile createMockMultipartFile(String fileName, String content) throws IOException {
        InputStream inputStream = IOUtils.toInputStream(content, StandardCharsets.UTF_8);
        return new MockMultipartFile("file", fileName, "text/plain", inputStream);
    }
}