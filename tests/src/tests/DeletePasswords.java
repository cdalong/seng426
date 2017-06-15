package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.DriverManager;
import java.util.concurrent.TimeUnit;

import util.ServerConfig;

public class DeletePasswords {

	private WebDriver driver;
	private String baseUrl = "http://localhost:8080";

	@Before
	public void setUp() throws Exception {
		ServerConfig.Setup(baseUrl);

		System.setProperty("webdriver.gecko.driver", "/home/limja/bin/geckodriver");

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
	public void test() {
		WebDriverWait wait = new WebDriverWait(driver, 5);

		driver.get(baseUrl + "/#/");
		driver.findElement(By.id("login")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("test@acme.com");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("test");
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.id("account-menu")));
		driver.findElement(By.linkText("ACMEPass")).click();

		String idBefore = driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
		driver.findElement(By.xpath("//tr[1]/td[7]/div/button[2]")).click();
		driver.findElement(By.cssSelector("button.btn.btn-danger")).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//pre[1]")));
		String message = driver.findElement(By.xpath("//pre[1]")).getText();
		String idAfter = driver.findElement(By.xpath("//tr[1]/td[1]")).getText();

		assertNotEquals(idBefore, idAfter);
		assert (message.contains(idBefore));
	}

}
