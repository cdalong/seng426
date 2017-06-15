package tests;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class CreateACMEPass {
    private WebDriver driver;
    private String baseUrl = "http://localhost:8080";
	
    @Before
    public void setUp(){
            System.setProperty("webdriver.gecko.driver", "src/geckodriver");

            driver = new FirefoxDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);        
            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);

            driver.get(baseUrl);
            
            //sign in with employee account
	    driver.findElement(By.id("login")).click();
	    driver.findElement(By.id("username")).clear();
    	    driver.findElement(By.id("username")).sendKeys("frank.paul@acme.com");
    	    driver.findElement(By.id("password")).clear();
    	    driver.findElement(By.id("password")).sendKeys("starwars");
    	    driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	    
    	    // We have to wait for the login to complete before performing the
            // nagivation so that the navbar can refresh
            Wait<WebDriver> wait = new FluentWait<>(this.driver)
                    .withTimeout(10, TimeUnit.SECONDS)
                    .pollingEvery(500, TimeUnit.MILLISECONDS)
                    .ignoring(NoSuchElementException.class);
            
            WebElement pass = wait.until((driver) -> driver.findElement(By.linkText("ACMEPass")));
            pass.click();
    }
	
    @Test
    public void testCreateACME() {	
	int rows_before = driver.findElements(By.xpath("//table[@class='jh-table table table-striped']/tbody/tr")).size();
		
	// create test entry
        driver.findElement(By.cssSelector("button[href='#/acme-pass/new']")).click();
        driver.findElement(By.cssSelector("input#field_site")).sendKeys("test site");
        driver.findElement(By.cssSelector("input#field_login")).sendKeys("test login");
        driver.findElement(By.cssSelector("input#field_password")).sendKeys("test pass");
        driver.findElement(By.cssSelector("button[type=submit]")).click();
        
        // Sleep to give the page time to populate
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            System.out.println("Sleep interrupted");
        }
        
        int rows_after = driver.findElements(By.xpath("//table[@class='jh-table table table-striped']/tbody/tr")).size();
        
        //assert if new entry is added to table
	assertEquals(rows_before+1, rows_after);
    }
	
    @After
    	public void tearDown(){
		driver.quit();
    }
}
