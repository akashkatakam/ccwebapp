package edu.northeastern.ccwebapp.s3PreSigned;
import java.io.IOException;
import java.net.URL;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public class S3GeneratePreSignedURL {

	public String getPreSignedURL(String objKey, String domainName) throws IOException {
        String clientRegion = "us-east-1";
        URL url=null;

        try {            
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion) .withCredentials(new ProfileCredentialsProvider()) .build();
    
            // Set the presigned URL to expire after 2min.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 2;
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL.
            System.out.println("Generating pre-signed URL.");
            GeneratePresignedUrlRequest generatePresignedUrlRequest =  new GeneratePresignedUrlRequest(domainName, objKey).withMethod(HttpMethod.GET) .withExpiration(expiration);
            url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            System.out.println("Pre-Signed URL: " + url.toString());
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        return url.toString();
    }
}
