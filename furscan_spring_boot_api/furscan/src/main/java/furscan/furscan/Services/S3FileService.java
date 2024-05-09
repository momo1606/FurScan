package furscan.furscan.Services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;

@Service
public class S3FileService {
     @Autowired
     private AmazonS3 amazonS3;

     @Value("${aws.s3.bucketName}")
     private String bucketName;

     public void setAmazonS3(AmazonS3 amazonS3){
          this.amazonS3 = amazonS3;
     }

     /**
      * Upload the pet's image on the S3.
      * @param file
      * @return
      */
     public String uploadFile(MultipartFile file){
          File modifiedFileName = new File(file.getOriginalFilename());
          try {
               String saveFile = convertMultipartToFile(file);
               String objectKey = "dog_images/" + saveFile;
               amazonS3.putObject(bucketName, objectKey, modifiedFileName);
               return saveFile;
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
     }

     /**
      * Convert Multipart to the file
      * @param file
      * @return
      * @throws IOException
      */     
     private String convertMultipartToFile(MultipartFile file) throws IOException {
          File newFile = new File(file.getOriginalFilename());
          FileOutputStream os = new FileOutputStream(newFile);
          os.write(file.getBytes());
          os.close();
          String fileName = System.currentTimeMillis() + "_" + newFile;
          return fileName;
     }
}
