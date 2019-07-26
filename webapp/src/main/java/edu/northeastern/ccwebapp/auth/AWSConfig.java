package edu.northeastern.ccwebapp.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWSConfig {
	
	String region="us-east-1";
	
	@Bean
	public AmazonS3 s3client() {
		
		AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
        		.withRegion(region)
        		.withCredentials(new DefaultAWSCredentialsProviderChain())
        		.build();
        return amazonS3Client;
	}
}
