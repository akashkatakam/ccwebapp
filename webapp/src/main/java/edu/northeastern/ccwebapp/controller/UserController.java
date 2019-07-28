package edu.northeastern.ccwebapp.controller;

import edu.northeastern.ccwebapp.pojo.User;
import edu.northeastern.ccwebapp.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.timgroup.statsd.StatsDClient;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
	
	@Autowired
	private StatsDClient stats;
	
    @Autowired
    private UserService userService;
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity basicAuth(HttpServletRequest req) {
    	stats.incrementCounter("endpoint.getuser.http.get");
        String headerResp = req.getHeader("Authorization");
        logger.info("You are in get user controller api");
        return userService.checkUserStatus(headerResp);
    }

    @PostMapping(value = "/user/register", produces = "application/json", consumes = "application/json")
    public ResponseEntity registerUser(@RequestBody User user) {
    	 stats.incrementCounter("endpoint.createuser.http.post");
    	 logger.info("You are in post user controller api");
        return userService.saveUser(user);
    }
    
    @PostMapping(value="/reset", produces = "application/json", consumes = "application/json")
    	public ResponseEntity resetPassword(@RequestBody User user) {
    	return userService.resetPassword(user);
    }
}
