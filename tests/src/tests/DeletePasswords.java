package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import util.ServerConfig;
import util.WebDriverFactory;

public class DeletePasswords {
	
	private WebDriver driver;
	private WebDriverWait wait;
	private String baseUrl = "http://localhost:8080";

	@Before
	public void setUp() throws Exception {
		if (System.getProperty("url") != null)
			baseUrl = System.getProperty("url");
		
		ServerConfig.Setup(baseUrl, 5);

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
	public void test() {
		
		driver.get(baseUrl + "/#/acme-pass");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[1]/td[1]")));
		
		// Open the delete password dialogue box and cofirm the delete.
		String idBefore = driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
		driver.findElement(By.xpath("//tr[1]/td[7]/div/button[2]")).click();
		driver.findElement(By.cssSelector("button.btn.btn-danger")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//pre[1]")));
		
		// Check both the feedback message and the id of the cell where the password was deleted.
		String message = driver.findElement(By.xpath("//pre[1]")).getText();
		String idAfter = driver.findElement(By.xpath("//tr[1]/td[1]")).getText();

		assertNotEquals(idBefore, idAfter);
		assertTrue(message.contains(idBefore));
	}

}
