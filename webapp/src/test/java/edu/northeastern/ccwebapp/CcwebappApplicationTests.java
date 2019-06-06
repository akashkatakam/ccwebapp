package edu.northeastern.ccwebapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import edu.northeastern.ccwebapp.pojo.Book;
import edu.northeastern.ccwebapp.service.BookService;
import net.minidev.json.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CcwebappApplicationTests {

    @Test
    public void contextLoads() {
    }
    
    @Test
    public void sampleTest() {
    	assertTrue(true);
    }
    
    @Test
    public void getBookDetail() {
    	String id = "1ccb503e-ac67-4e54-8472-2f89ab6af962";
    	BookService b= new BookService();
    	Book respBook = b.getBookById(id);
    	JSONObject j =  new JSONObject();
    	j.appendField("isbn","TIM123" );
    	j.appendField("author","Avicii" );
    	j.appendField("quantity",5000 );
    	j.appendField("title","Tim Bergling" );
    	
    	assertEquals(j, respBook);
    }

}
