package edu.northeastern.ccwebapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import edu.northeastern.ccwebapp.pojo.User;
import edu.northeastern.ccwebapp.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity basicAuth(HttpServletRequest req, HttpServletResponse resp) {
		String headerResp = req.getHeader("Authorization");
		ResponseEntity message = userService.checkUserStatus(headerResp);
		return message;
	}

	@PostMapping(value = "/user/register", produces = "application/json", consumes = "application/json")
	public ResponseEntity registerUser(@RequestBody User user) {
		return userService.saveUser(user);
	}
}
