package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import util.ServerConfig;
import util.WebDriverFactory;

public class EditPassword {

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl = "http://localhost:8080";

    @Before
    public void setup() throws Exception {
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

    @Test
    public void testEdit() throws Exception {

        driver.get(baseUrl + "/#/acme-pass");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[1]")));

        // Open the edit password dialogue box.
        String idBefore = driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
        String siteBefore = driver.findElement(By.xpath("//tr[1]/td[2]")).getText();
        String loginBefore = driver.findElement(By.xpath("//tr[1]/td[3]")).getText();
        String passwordBefore = driver.findElement(By.xpath("//tr[1]/td[4]/div/input")).getAttribute("value");
        driver.findElement(By.xpath("//tr[1]/td[7]/div/button[1]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type=submit]")));

        String newSite = siteBefore.length() < 20 ? siteBefore + "0" : siteBefore.substring(0, 19);
        String newLogin = loginBefore.length() < 20 ? loginBefore + "0" : loginBefore.substring(0, 19);
        String newPassword = passwordBefore.length() < 20 ? passwordBefore + "0" : passwordBefore.substring(0, 19);

        // Input new password data and confirm the edit.
        driver.findElement(By.id("field_site")).clear();
        driver.findElement(By.id("field_login")).clear();
        driver.findElement(By.id("field_password")).clear();
        driver.findElement(By.id("field_site")).sendKeys(newSite);
        driver.findElement(By.id("field_login")).sendKeys(newLogin);
        driver.findElement(By.id("field_password")).sendKeys(newPassword);
        driver.findElement(By.cssSelector("button[type=submit]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//pre[1]")));

        String message = driver.findElement(By.xpath("//pre[1]")).getText();
        String idAfter = driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
        String siteAfter = driver.findElement(By.xpath("//tr[1]/td[2]")).getText();
        String loginAfter = driver.findElement(By.xpath("//tr[1]/td[3]")).getText();
        String passwordAfter = driver.findElement(By.xpath("//tr[1]/td[4]/div/input")).getAttribute("value");

        assertEquals(idBefore, idAfter);
        assertEquals(siteAfter, newSite);
        assertEquals(loginAfter, newLogin);
        assertEquals(passwordAfter, newPassword);
        assertTrue(message.contains(idBefore));
    }

    @Test
    public void testEmptySite() throws Exception {
        driver.get(baseUrl + "/#/acme-pass");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[1]")));

        // Open the edit password dialogue box.
        driver.findElement(By.xpath("//tr[1]/td[7]/div/button[1]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type=submit]")));

        // Input new password data and confirm the edit.
        driver.findElement(By.id("field_site")).clear();
        WebElement submit = driver.findElement(By.cssSelector("button[type=submit]"));
        assertEquals("true", submit.getAttribute("disabled"));
    }

    @Test
    public void testEmptyLogin() throws Exception {
        driver.get(baseUrl + "/#/acme-pass");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[1]")));

        // Open the edit password dialogue box.
        driver.findElement(By.xpath("//tr[1]/td[7]/div/button[1]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type=submit]")));

        // Input new password data and confirm the edit.
        driver.findElement(By.id("field_login")).clear();
        WebElement submit = driver.findElement(By.cssSelector("button[type=submit]"));
        assertEquals("true", submit.getAttribute("disabled"));
    }

    @Test
    public void testEmptyPassword() throws Exception {
        driver.get(baseUrl + "/#/acme-pass");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[1]")));

        // Open the edit password dialogue box.
        driver.findElement(By.xpath("//tr[1]/td[7]/div/button[1]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type=submit]")));

        // Input new password data and confirm the edit.
        driver.findElement(By.id("field_password")).clear();
        WebElement submit = driver.findElement(By.cssSelector("button[type=submit]"));
        assertEquals("true", submit.getAttribute("disabled"));
    }

    @After
    public void finish() {
        this.driver.close();
    }

}
