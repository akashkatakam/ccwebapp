package edu.northeastern.ccwebapp.service;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class S3Service {
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new EnvironmentVariableCredentialsProvider())
            .build();

    public List<Bucket> getAllBuckets() {
        return s3Client.listBuckets();
    }


}