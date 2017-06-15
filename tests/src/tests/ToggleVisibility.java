package tests;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import junit.framework.TestCase;

public class ToggleVisibility extends TestCase{
	
	private WebDriver driver;
	@Before
	public void setUp() throws Exception{
		
		System.setProperty("webdriver.chrome.driver", "C:/Users/Crims/Documents/chromedriver_win32/chromedriver.exe");

		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
	}
	
	public void testLogin() throws Exception{
		
		this.driver.get("http://localhost:8080");
		
		WebElement login = driver.findElement(By.id("login"));
		login.click();
		
		WebElement username = driver.findElement(By.id("username"));
		
		WebElement password = driver.findElement(By.id("password"));
		
		username.sendKeys("jo.thomas@acme.com");
		
		password.sendKeys("12345");
		
		WebElement submit = driver.findElement(By.id("submit"));
		
		
		submit.click();	
		
	}
	
	public void testVisibility() throws Exception{
		
		
			testLogin();
		
		
			driver.get("http://localhost:8080/#/acme-pass");
			
			driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr[1]/td[4]/div/span")).click();
			
			WebElement password = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr[1]/td[4]/div/input"));
			
			System.out.println(password.getAttribute("value"));
			
			
			assertEquals("!U1I49wi", password.getAttribute("value"));
			
			
			
		//whoop whoop
			
		
	}
	
	@After
	public void tearDown() throws Exception {
		this.driver.quit();
	}

}
