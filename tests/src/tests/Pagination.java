package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import util.ServerConfig;
import util.WebDriverFactory;

public class Pagination {

	private WebDriver driver;
	private WebDriverWait wait;
	private String baseUrl = "http://localhost:8080";
	
	@Before
	public void setUp() throws Exception {
		if (System.getProperty("url") != null)
			baseUrl = System.getProperty("url");
		
		ServerConfig.Setup(baseUrl, 45);

		driver = WebDriverFactory.Create();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 5);
		
		driver.get(baseUrl + "/#/");
		driver.findElement(By.id("login")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("test@acme.com");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("test");
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("account-menu")));
	}
	
	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
	
	@Test
	public void test() throws Exception {

		driver.get(baseUrl + "/#/acme-pass");

		By navLocator = By.xpath("/html/body/div[3]/div/div/div[3]/jhi-item-count/div");
		wait.until(ExpectedConditions.elementToBeClickable(navLocator));
		String navMessage = driver.findElement(navLocator).getText();

		// Get the total number of items, items shown and pages.
		String[] stringArray = navMessage.split(" "); 		// showing x - y of z items
		int totalnumberofrecords = Integer.parseInt(stringArray[5]);
		int recordsperpage = Integer.parseInt(stringArray[3]);
		int clickcount = (totalnumberofrecords - 1) / recordsperpage;
		
		// Step forward through each page and record the page indices.
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
		
		// Step backwards through each page and record the page indices.
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
