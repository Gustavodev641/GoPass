package com.GoPass.GoPass.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Value("${aws.accessKey:}")
    private String accessKey;

    @Value("${aws.secretKey:}")
    private String secretKey;

    @Value("${aws.region:us-east-2}")
    private String region;

    @Bean
    public AmazonS3 s3Client() {
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                .withRegion(region);


        if (accessKey != null && !accessKey.isBlank()
                && secretKey != null && !secretKey.isBlank()) {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            builder.withCredentials(new AWSStaticCredentialsProvider(credentials));
        } else {
            builder.withCredentials(InstanceProfileCredentialsProvider.getInstance());
        }

        return builder.build();
    }
}