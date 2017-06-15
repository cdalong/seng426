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

public class EditPassword {

    private WebDriver driver;
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
    }

    @Test
    public void testEdit() throws Exception {
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
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[1]")));
		String idBefore = driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
		driver.findElement(By.xpath("//tr[1]/td[7]/div/button[1]")).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type=submit]")));
		driver.findElement(By.id("field_site")).clear();
		driver.findElement(By.id("field_login")).clear();
		driver.findElement(By.id("field_password")).clear();
		driver.findElement(By.id("field_site")).sendKeys("edit_foo");
		driver.findElement(By.id("field_login")).sendKeys("edit_foo2");
		driver.findElement(By.id("field_password")).sendKeys("edit_foo3");
		driver.findElement(By.cssSelector("button[type=submit]")).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//pre[1]")));
		String message = driver.findElement(By.xpath("//pre[1]")).getText();
		String idAfter = driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
		String siteAfter = driver.findElement(By.xpath("//tr[1]/td[2]")).getText();
		String loginAfter = driver.findElement(By.xpath("//tr[1]/td[3]")).getText();
		String passwordAfter = driver.findElement(By.xpath("//tr[1]/td[4]/div/input")).getAttribute("value");
        
		assertEquals(idBefore, idAfter);
		assertEquals(siteAfter, "edit_foo");
		assertEquals(loginAfter, "edit_foo2");
		assertEquals(passwordAfter, "edit_foo3");
		assertTrue(message.contains(idBefore));
    }

    @After
    public void finish() {
        this.driver.close();
    }

}
