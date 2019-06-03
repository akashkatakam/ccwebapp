package edu.northeastern.ccwebapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.northeastern.ccwebapp.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/", produces = "application/json")
	public String basicAuth(HttpServletRequest req, HttpServletResponse resp) {
		String headerResp = req.getHeader("Authorization");
		String message= userService.checkUserStatus(headerResp);
		return message;
	}

    @PostMapping(value = "/user/register", produces = "application/json" , consumes ="application/json" )
    public ResponseEntity registerUser(@RequestBody UserDetails user) {
        return userService.saveUser(user);
    }
}
