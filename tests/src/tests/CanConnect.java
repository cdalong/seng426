package tests;

import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import junit.framework.TestCase;

public class CanConnect extends TestCase{
	
	private WebDriver driver;
	
	@Before
	public void setUp() throws Exception{
		
		System.setProperty("webdriver.chrome.driver", "C:/Users/Crims/Documents/chromedriver_win32/chromedriver.exe");

		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		
	}
	
	public void testConnect() throws Exception{
		
		this.driver.get("http://localhost:8080");
		
		assertEquals("ACME Security Systems", this.driver.getTitle());
		
	}
	
	@After
	public void tearDown() throws Exception {
		this.driver.quit();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
