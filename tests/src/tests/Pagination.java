package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import util.ServerConfig;

public class Pagination {

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
		ServerConfig.Setup(baseUrl, 45);

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
	}
	
	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
	
	@Test
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

		By navLocator = By.xpath("/html/body/div[3]/div/div/div[3]/jhi-item-count/div");
		wait.until(ExpectedConditions.elementToBeClickable(navLocator));
		String navMessage = driver.findElement(navLocator).getText();

		// showing x - y of z items
		String[] stringArray = navMessage.split(" ");
		int totalnumberofrecords = Integer.parseInt(stringArray[5]);
		int recordsperpage = Integer.parseInt(stringArray[3]);
		int clickcount = (totalnumberofrecords - 1) / recordsperpage;
		
		ArrayList<Integer> pageIndicesFwd = new ArrayList<Integer>();
		pageIndicesFwd.add(Integer.parseInt(stringArray[1]));
		for (int i = 0; i < clickcount; i++) {	
			driver.findElement(By.xpath("/html/body/div[3]/div/div/ul/li[2]/a")).click();
			wait.until(ExpectedConditions.elementToBeClickable(navLocator));
			wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(navLocator, navMessage)));
			navMessage = driver.findElement(navLocator).getText();
			stringArray = navMessage.split(" ");
			pageIndicesFwd.add(Integer.parseInt(stringArray[1]));
		}
		
		ArrayList<Integer> pageIndicesBack = new ArrayList<Integer>();
		pageIndicesBack.add(Integer.parseInt(stringArray[1]));
		for (int i = 0; i < clickcount; i++) {	
			driver.findElement(By.xpath("/html/body/div[3]/div/div/ul/li[1]/a")).click();
			wait.until(ExpectedConditions.elementToBeClickable(navLocator));
			wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(navLocator, navMessage)));
			navMessage = driver.findElement(navLocator).getText();
			stringArray = navMessage.split(" ");
			pageIndicesBack.add(Integer.parseInt(stringArray[1]));
		}
		
		for (int i = 0; i < pageIndicesFwd.size(); ++i)
			assertEquals((int)pageIndicesFwd.get(i), 1 + i * recordsperpage);
		for (int i = 0; i < pageIndicesFwd.size(); ++i)
			assertEquals((int)pageIndicesBack.get(i), 1 + (pageIndicesFwd.size() - i - 1) * recordsperpage);
	}

}
