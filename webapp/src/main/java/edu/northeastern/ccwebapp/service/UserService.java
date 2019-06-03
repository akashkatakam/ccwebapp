package edu.northeastern.ccwebapp.service;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.northeastern.ccwebapp.pojo.User;
import edu.northeastern.ccwebapp.repository.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String validateUserDetails(UserDetails user) {

        if(user.getUsername() == null || user.getUsername().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty()) {
            return "username and password cannot be empty.";
        }

        String regExpression = "^([a-zA-Z0-9_.+-])+\\@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        Pattern pattern = Pattern.compile(regExpression);
        Matcher matcher = pattern.matcher(user.getUsername());

        if (!matcher.matches()) {
            return "Please enter a valid email address.";
        }

        regExpression = "^[a-zA-Z]\\w{3,14}$";
        //"^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d-]{8,}$";
        /*The password's first character must be a letter, it must contain at least 4 characters and
        no more than 15 characters and no characters other than letters,
        numbers and the underscore may be used*/

        pattern = Pattern.compile(regExpression);
        matcher = pattern.matcher(user.getUsername());

        if (!matcher.matches()) {
            return "Please enter a valid password.";
        }
        return "success";
    }

    public ResponseEntity saveUser(UserDetails user) {

        String errorMessage = validateUserDetails(user);
        if(errorMessage.equalsIgnoreCase("success")) {

            UserDetails ud = new UserDetails();
            ud.setUsername(user.getUsername());
            ud.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

            UserDetails userByUsername = this.findByUserName(user.getUsername());
            if (userByUsername != null) {
                return new ResponseEntity("Username already exist, please enter another one.", HttpStatus.CONFLICT);
            }
            userRepository.save(ud);
            return new ResponseEntity("User registered successfully.", HttpStatus.OK);

        } else {
            return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
        }

    }

    public UserDetails findByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
