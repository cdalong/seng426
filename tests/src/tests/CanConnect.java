package tests;

import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.WebDriver;

import util.WebDriverFactory;
import junit.framework.TestCase;

public class CanConnect extends TestCase{
	
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
	
	public void testConnect() throws Exception {
		
		driver.get(baseUrl);
		
		assertEquals(driver.getTitle(), "ACME Security Systems");
		
	}
}
