package tests;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import util.ServerConfig;
import util.WebDriverFactory;

public class GeneratePassword {
	
	private WebDriver driver;
	private String baseUrl = "http://localhost:8080";

	@Before
	public void setUp() throws Exception {
		if (System.getProperty("url") != null)
			baseUrl = System.getProperty("url");
		
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

	@Test
	public void test() {
		WebDriverWait wait = new WebDriverWait(driver, 5);

		// Login.
		driver.get(baseUrl + "/#/");
		driver.findElement(By.id("login")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("test@acme.com");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("test");
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();

		// Navigate to ACMEPass page.
		wait.until(ExpectedConditions.elementToBeClickable(By.id("account-menu")));
		driver.findElement(By.linkText("ACMEPass")).click();
		
		// Open the generate password dialogue box.
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[href='#/acme-pass/new']")));
	    driver.findElement(By.cssSelector("button[href='#/acme-pass/new']")).click();
	    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[ng-click='vm.openPwdGenModal()']")));
	    driver.findElement(By.cssSelector("button[ng-click='vm.openPwdGenModal()']")).click();
	    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[ng-click='vm.generate()']")));
	    
	    // Record the requested length for generated passwords.
	    String lenString = driver.findElement(By.id("field_length")).getAttribute("value");
	    int defaultLength = Integer.parseInt(lenString);

	    WebElement passwordField = driver.findElement(By.id("field_password"));
	    
	    // Generate a password with all characters.
	    driver.findElement(By.cssSelector("button[ng-click='vm.generate()']")).click();
	    wait.until(ExpectedConditions.attributeToBeNotEmpty(passwordField, "value"));
	    String generated1 = passwordField.getAttribute("value");
	    
	    // Generate a password without special characters.
	    driver.findElement(By.id("field_special")).click();
	    driver.findElement(By.cssSelector("button[ng-click='vm.generate()']")).click();
	    wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(passwordField, "value", generated1)));
	    String generated2 = passwordField.getAttribute("value");
	    
	    // Generate a password without special characters or digits.
	    driver.findElement(By.id("field_digits")).click();
	    driver.findElement(By.cssSelector("button[ng-click='vm.generate()']")).click();
	    wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(passwordField, "value", generated2)));
	    String generated3 = passwordField.getAttribute("value");
	    
	    // Generate a password without special characters, digits, or uppercase letters.
	    driver.findElement(By.id("field_upper")).click();
	    driver.findElement(By.cssSelector("button[ng-click='vm.generate()']")).click();
	    wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(passwordField, "value", generated3)));
	    String generated4 = passwordField.getAttribute("value");
	    
	    // Generate a password with no options enabled.
	    driver.findElement(By.id("field_lower")).click();
	    driver.findElement(By.cssSelector("button[ng-click='vm.generate()']")).click();
	    wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(passwordField, "value", generated4)));
	    String generated5 = passwordField.getAttribute("value");

	    // Generate a 10 digit password with no repetitions.
	    driver.findElement(By.id("field_digits")).click();
	    driver.findElement(By.id("field_repetition")).click();
	    driver.findElement(By.id("field_length")).clear();
	    driver.findElement(By.id("field_length")).sendKeys("10");
	    driver.findElement(By.cssSelector("button[ng-click='vm.generate()']")).click();
	    wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(passwordField, "value", generated5)));
	    String generated6 = passwordField.getAttribute("value");

	    // Confirm the generated password.
	    driver.findElement(By.cssSelector("button[type='submit']")).click();
	    String accepted = driver.findElement(By.id("field_password")).getAttribute("value");
	    
	    String lower = "abcdefghijklmnopqrstuvwxyz";
	    String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    String digits = "01234567890";
	    String special = "!@#$%-_";
	    
	    assertTrue(generated1.length() == defaultLength);
	    assertTrue(generated2.length() == defaultLength);
	    assertTrue(generated3.length() == defaultLength);
	    assertTrue(generated4.length() == defaultLength);
	    assertTrue(generated5.length() == 0);
	    assertTrue(generated6.length() == 10);
	    assertTrue(generated2.chars().allMatch(x -> special.indexOf(x) == -1));
	    assertTrue(generated3.chars().allMatch(x -> digits.indexOf(x) == -1));
	    assertTrue(generated4.chars().allMatch(x -> upper.indexOf(x) == -1));
	    assertTrue(generated5.chars().allMatch(x -> lower.indexOf(x) == -1));
	    assertTrue(generated6.chars().allMatch(x -> digits.indexOf(x) != -1));
	    assertTrue(digits.chars().allMatch(x -> StringUtils.countMatches(generated6, (char)x) == 1));
	    assertEquals(generated6, accepted);
	}	
}