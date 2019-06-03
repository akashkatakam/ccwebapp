package edu.northeastern.ccwebapp.service;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.northeastern.ccwebapp.pojo.User;
import edu.northeastern.ccwebapp.repository.UserRepository;


@Service
public class UserService {
	private UserRepository userRepository;
	
	@Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	public String checkUserStatus(String headerResp) {
    	String message = null;
    	if(headerResp.contains("Basic")) {
    			String[] user =  new String(Base64.getDecoder().decode(headerResp.substring(6).getBytes())).split(":", 2);//decode the header and split into username and password
    			User u = null ;//= findByUserName(user[0]);//find it by username
    			if(u!=null) {
    				if(new BCryptPasswordEncoder().matches(user[1], u.getPassword())) {//check for password match
    					message= "Current time - "+new Date();
    				}
    				else {
    					message="Credentials are not right";
    				}
    			}
    			
    			else {
    				message="User does not exist";
    			}
    	}
    	else {
    		message="User is not logged in";
    	}
    	return message;
    }
}
