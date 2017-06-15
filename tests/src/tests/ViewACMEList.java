package tests;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import util.ServerConfig;

public class ViewACMEList {
	
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
		ServerConfig.Setup(baseUrl, 15);

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
    }

    @After
	public void tearDown(){
		driver.quit();
    }
    
    @Test
    public void testViewACME() {
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
		
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.table-responsive")));
		By elementLocator = By.xpath("//tr[@ng-repeat='acmePass in vm.acmePasses track by acmePass.id']");
		By navLocator = By.xpath("/html/body/div[3]/div/div/div[3]/jhi-item-count/div");
		
		String navMessage = driver.findElement(navLocator).getText(); // showing x - y of z items
		String[] parts = navMessage.split(" ");
		int elementsReported = Integer.parseInt(parts[3]);
		
		List<WebElement> elementsListed = driver.findElements(elementLocator);
		
		assertEquals(elementsListed.size(), elementsReported);
    }
}

