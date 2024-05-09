package furscan.furscan.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

     @Value("${aws.accessKey}")
     private String accessKey;

     @Value("${aws.secretKey}")
     private String secretKey;

     @Value("${aws.region}")
     private String region;

     /**
      * Building the AWS credentials
      * @return
      */
     @Bean
     public AmazonS3 s3() {
          AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
          AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

          return AmazonS3ClientBuilder
                  .standard()
                  .withRegion(region)
                  .withCredentials(awsStaticCredentialsProvider)
                  .build();

     }
}
