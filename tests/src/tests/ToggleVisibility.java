package tests;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import util.ServerConfig;
import util.WebDriverFactory;
import junit.framework.TestCase;

public class ToggleVisibility extends TestCase{
	
	private static String baseUrl = "http://localhost:8080";

	private WebDriver driver;
	
	@BeforeClass
	public static void setUpOnce() throws Exception {
		if (System.getProperty("webdriver.gecko.driver") == null)
			System.setProperty("webdriver.gecko.driver", "bin/geckodriver");

		if (System.getProperty("url") != null)
			baseUrl = System.getProperty("url");
	}
	
	@Before
	public void setUp() throws Exception {
		ServerConfig.Setup(baseUrl, 5);

		driver = WebDriverFactory.Create();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
	}
	
	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
	
	public void test() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 5);
		
		driver.get(baseUrl + "/#/");
		driver.findElement(By.id("login")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("test@acme.com");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("test");
		driver.findElement(By.id("login")).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("account-menu")));
		driver.findElement(By.linkText("ACMEPass")).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[1]/td[4]/div")));
		WebElement span = driver.findElement(By.xpath("//tr[1]/td[4]/div/span"));
		WebElement input = driver.findElement(By.xpath("//tr[1]/td[4]/div/input"));;
		String type1 = input.getAttribute("type");
		String class1 = span.getAttribute("class");
		span.click();
		
		wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(input, "type", type1)));
		String type2 = input.getAttribute("type");
		String class2 = span.getAttribute("class");
		span.click();
		
		wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(input, "type", type2)));
		String type3 = input.getAttribute("type");
		String class3 = span.getAttribute("class");
		
		assertNotEquals(type1, type2);
		assertNotEquals(type2, type3);
		assertEquals(type1, type3);
		assertNotEquals(class1, class2);
		assertNotEquals(class2, class3);
		assertEquals(class1, class3);
	}
}
