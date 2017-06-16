package tests;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.WebDriver;

import util.WebDriverFactory;

public class CanConnect {
	
	private WebDriver driver;
	private String baseUrl = "http://localhost:8080";

	@Before
	public void setUp() throws Exception {
		if (System.getProperty("url") != null)
			baseUrl = System.getProperty("url");
		
		driver = WebDriverFactory.Create();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	}
	
	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
	
	@Test
	public void testConnect() throws Exception {
		
		driver.get(baseUrl);
		
		assertEquals(driver.getTitle(), "ACME Security Systems");
		
	}
}
