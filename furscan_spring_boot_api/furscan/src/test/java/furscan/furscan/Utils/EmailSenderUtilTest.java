package furscan.furscan.Utils;

import org.springframework.mail.SimpleMailMessage;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderUtilTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailSenderUtil emailSenderUtil;

    @Test
    void sendEmailWithoutAttachmentTest() {
        String to = "test@example.com";
        String body = "body";
        String subject = "subject";
        emailSenderUtil.sendEmailWithoutAttachment(to, body, subject);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
    @Test
     public void sendEmailWithAttachmentTest() {

        String to = "test@example.com";
        String body = "body";
        String subject = "subject";
        String attachment = "https://furscan.com/report.pdf";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailSenderUtil.sendEmailWithAttachment(to, body, subject, attachment);
        verify(mailSender).send(mimeMessage);
    }

}
