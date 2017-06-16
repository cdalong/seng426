package tests;

import static org.junit.Assert.*;

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

public class CreateACMEPass {
	
	private WebDriver driver;
	private WebDriverWait wait;
	private String baseUrl = "http://localhost:8080";

	@Before
	public void setup() throws Exception {
		if (System.getProperty("url") != null)
			baseUrl = System.getProperty("url");

		ServerConfig.Setup(baseUrl, 5);

		driver = WebDriverFactory.Create();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
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
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void testCreateACME() {
		
		driver.get(baseUrl + "/#/acme-pass");
		By navLocator = By.xpath("/html/body/div[3]/div/div/div[3]/jhi-item-count/div");
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[href='#/acme-pass/new']")));
		
		String navMessage = driver.findElement(navLocator).getText(); // showing x - y of z items
		String[] parts = navMessage.split(" ");
		int countBefore = Integer.parseInt(parts[5]);
		
		// create test entry
		driver.findElement(By.cssSelector("button[href='#/acme-pass/new']")).click();
		driver.findElement(By.cssSelector("input#field_site")).sendKeys("test site");
		driver.findElement(By.cssSelector("input#field_login")).sendKeys("test login");
		driver.findElement(By.cssSelector("input#field_password")).sendKeys("test pass");
		driver.findElement(By.cssSelector("button[type=submit]")).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//pre[1]")));
		String createMessage = driver.findElement(By.xpath("//pre[1]")).getText();
		
		navMessage = driver.findElement(navLocator).getText();
		parts = navMessage.split(" ");
		int countAfter = Integer.parseInt(parts[5]);
		
		// order by id descending so the newly created password is at the top.
		driver.findElement(By.xpath("//th[@jh-sort-by='id']")).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath("//th[@jh-sort-by='id']/span[2]"), "class", "glyphicon-sort-by-attributes"));
		
		String idNew = driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
		String siteNew = driver.findElement(By.xpath("//tr[1]/td[2]")).getText();
		String loginNew = driver.findElement(By.xpath("//tr[1]/td[3]")).getText();
		String passwordNew = driver.findElement(By.xpath("//tr[1]/td[4]/div/input")).getAttribute("value");
		
		// assert if new entry is added to table
		assertEquals(countBefore + 1, countAfter);
		assertEquals(siteNew, "test site");
		assertEquals(loginNew, "test login");
		assertEquals(passwordNew, "test pass");
		assertTrue(createMessage.contains(idNew));
	}

}
