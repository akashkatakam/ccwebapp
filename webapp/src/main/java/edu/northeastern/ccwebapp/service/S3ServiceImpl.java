package edu.northeastern.ccwebapp.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3ServiceImpl {

	@Autowired
	private AmazonS3 s3client;
	/*
	 * @Value("${bucket.name}") private String bucketName;
	 */
	static String domainName = "kallurit";
	static String bucketName = "csye6225-su19-"+domainName+".me.csye6225.com";
	
	public S3ServiceImpl() {
		this.s3client = S3Config.amazonS3Client();
	}
	public void uploadFile(String keyName, MultipartFile uploadFile) throws IllegalStateException, IOException {
		try {	
		    File convFile = new File(System.getProperty("java.io.tmpdir") +
		    System.getProperty("file.separator") + uploadFile.getOriginalFilename());
			uploadFile.transferTo(convFile);
			/*convFile: path of the file to upload*/
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, convFile);
			/*
			 * AccessControlList acl = new AccessControlList();
			 * acl.grantPermission(GroupGrantee.AllUsers, Permission.Read); //all users or
			 * authenticated putObjectRequest.setAccessControlList(acl);
			 */
			s3client.putObject(putObjectRequest);  
		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
		} catch (AmazonClientException ace) {
			ace.getMessage();
		}
	}
	
	public void deleteFile(String keyName) {
		try {
			s3client.deleteObject(bucketName, keyName);
		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
		} catch (AmazonClientException ace) {
			ace.getMessage();
		}

	}
}
