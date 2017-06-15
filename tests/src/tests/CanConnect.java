package tests;

import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.WebDriver;

import util.WebDriverFactory;
import junit.framework.TestCase;

public class CanConnect extends TestCase{
	
	private static String baseUrl = "http://localhost:8080";

	private WebDriver driver;
	
	@BeforeClass
	public static void setUpOnce() throws Exception {
		if (System.getProperty("url") != null)
			baseUrl = System.getProperty("url");
	}
	
	@Before
	public void setUp() throws Exception {
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
