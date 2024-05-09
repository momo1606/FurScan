package furscan.furscan.Utils;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderUtil {
     
     @Autowired
     private JavaMailSender mailSender;

     /**
      * To send the Email with Attachment
      * @param to
      * @param body
      * @param subject
      * @param attachment
      */
     public void sendEmailWithAttachment(String to, String body, String subject, String attachment) {
          try {
               MimeMessage mimeMessage = mailSender.createMimeMessage();
               MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
               messageHelper.setFrom("rahulgoswami.rg2000@gmail.com");
               messageHelper.setTo(to);
               messageHelper.setText(body);
               messageHelper.setSubject(subject);

               UrlResource urlResource = new UrlResource(attachment); 
               messageHelper.addAttachment(urlResource.getFilename(), urlResource);
               mailSender.send(mimeMessage);
          } catch (MessagingException e) {
               e.printStackTrace();
          } catch (MalformedURLException e) {
               e.printStackTrace();
          }
     }

     /**
      * To send the email without attachment.
      * @param to
      * @param body
      * @param subject
      */
     public void sendEmailWithoutAttachment(String to, String body, String subject) {
          try {
               SimpleMailMessage message = new SimpleMailMessage();
               message.setFrom("rahulgoswami.rg2000@gmail.com");
               message.setTo(to);
               message.setText(body);
               message.setSubject(subject);
               mailSender.send(message);
          } catch (Exception e) {
               e.printStackTrace();
          }
     }
}


