package edu.northeastern.ccwebapp.service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import edu.northeastern.ccwebapp.pojo.User;
import edu.northeastern.ccwebapp.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public ResponseEntity<Map<String, String>> checkUserStatus(String headerResp) {
		ResponseEntity<Map<String, String>> message;
		Map<String, String> jsonResponse = new HashMap<String, String>();

    	if(headerResp != null && headerResp.contains("Basic")) {
    			String[] userDetails =  new String(Base64.getDecoder().decode(headerResp.substring(6).getBytes())).split(":", 2);//decode the header and split into username and password
    			User user = findByUserName(userDetails[0]);//find it by username
    			if(user != null) {
    				if(new BCryptPasswordEncoder().matches(userDetails[1], user.getPassword())) {//check for password match
    					jsonResponse.put("message", "Current time - " + new Date());
    					message = new ResponseEntity<Map<String, String>>(jsonResponse, HttpStatus.OK);
    					
    				}
    				else {
    					jsonResponse.put("message", "Credentials are not right");
    					message = new ResponseEntity<Map<String, String>>(jsonResponse, HttpStatus.UNAUTHORIZED);
    				}
    			}
    			
    			else {
    				jsonResponse.put("message", "User does not exist");
    				message = new ResponseEntity<Map<String, String>>(jsonResponse, HttpStatus.UNAUTHORIZED);
    			}
    	}
    	else {
    		jsonResponse.put("message", "User is not logged in");
    		message = new ResponseEntity<Map<String, String>>(jsonResponse, HttpStatus.BAD_REQUEST) ;
    	}
    	return message;
    }

    private String validateUser(User user) {

        if(user.getUsername() == null || user.getUsername().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty()) {
            return "username and password cannot be empty.";
        }

        String regExpression = "^([a-zA-Z0-9_.+-])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        Pattern pattern = Pattern.compile(regExpression);
        Matcher matcher = pattern.matcher(user.getUsername());

        if (!matcher.matches()) {
            return "Please enter a valid email address.";
        }

        regExpression = "^[a-zA-Z0-9]\\w{3,14}$";
        //"^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d-]{8,}$";
        /*The password's first character must be a letter, it must contain at least 4 characters and
        no more than 15 characters and no characters other than letters,
        numbers and the underscore may be used*/

        pattern = Pattern.compile(regExpression);
        matcher = pattern.matcher(user.getPassword());

        if (!matcher.matches()) {
            return "Please enter a valid password.";
        }
        return "success";
    }

    public ResponseEntity<Map<String, String>> saveUser(User user) {

        String responseMessage = validateUser(user);
        Map<String, String> jsonResponse = new HashMap<String, String>();
        
        if(responseMessage.equalsIgnoreCase("success")) {
            User ud = new User();
            ud.setUsername(user.getUsername());
            ud.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

            User userByUsername = this.findByUserName(user.getUsername());
            if (userByUsername != null) {
            	jsonResponse.put("message", "Username already exist, please enter another one.");
                return new ResponseEntity<Map<String, String>>(jsonResponse, HttpStatus.CONFLICT);
            }
            userRepository.save(ud);
            jsonResponse.put("message", "User registered successfully.");
            return new ResponseEntity<Map<String, String>>(jsonResponse, HttpStatus.OK);

        } else {
        	jsonResponse.put("message", responseMessage);
            return new ResponseEntity<Map<String, String>>(jsonResponse, HttpStatus.BAD_REQUEST);
        }

    }

    private User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
