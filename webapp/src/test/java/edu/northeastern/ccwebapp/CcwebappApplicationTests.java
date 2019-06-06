package edu.northeastern.ccwebapp;

import edu.northeastern.ccwebapp.pojo.User;
import edu.northeastern.ccwebapp.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CcwebappApplicationTests {

	@Mock
	UserService userService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		User createUser = new User();
		createUser.setUsername("rachel@gmail.com");
		createUser.setPassword("rachel321");
		when(userService.findByUserName("rachel@gmail.com")).thenReturn(createUser);
	}

	@Test
	public void register() {
		User receivedUser = userService.findByUserName("rachel@gmail.com");
		assertEquals(receivedUser.getUsername(), "rachel@gmail.com");
	}

}
