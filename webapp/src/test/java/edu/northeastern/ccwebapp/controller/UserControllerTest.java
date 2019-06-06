package edu.northeastern.ccwebapp.controller;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import edu.northeastern.ccwebapp.pojo.User;
import edu.northeastern.ccwebapp.repository.UserRepository;

@RunWith(SpringRunner.class)
public class UserControllerTest {

	private MockMvc mockMvc;
    @InjectMocks
    UserController userController;
    
    @Mock
    UserRepository userRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    
	 @Test
	    public void addUser() throws Exception {
	        String jsonString = "{\n" +
	        		"\"id\":31,\n" +
	                "\"username\":\"rachel@gmail.com\",\n" +
	                "\"password\":\"rachel321\"\n" +
	                "}";
	        //User user = new User("rachel@gmail.com","rachel321");
	        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(jsonString))
	                .andExpect(MockMvcResultMatchers.status().isCreated())
	                .andExpect(MockMvcResultMatchers.jsonPath("$.username",Matchers.is("rachel@gmail.com")))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.password",Matchers.is("rachel321")));
	    }

}
