package tests;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import util.WebDriverFactory;

public class EditPassword {

    private WebDriver driver;

    @Before
    public void setup() {
        this.driver = WebDriverFactory.Create();
        this.driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
        this.driver.get("http://localhost:8080");

        // Setup the test by logging in to the site as a test user and
        // navigating to the ACMEPass page
        this.driver.findElement(By.id("login")).click();
        this.driver.findElement(By.cssSelector("input#username")).sendKeys("paul.robert@acme.com");
        this.driver.findElement(By.cssSelector("input#password")).sendKeys("shadow");
        this.driver.findElement(By.cssSelector("button[type=submit]")).click();

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
    public void testEdit() throws Exception {
        // first we create a test entry for us to edit
        this.driver.findElement(By.cssSelector("button[ui-sref='acme-pass.new']")).click();
        this.driver.findElement(By.cssSelector("input#field_site")).sendKeys("edit_test.site");
        this.driver.findElement(By.cssSelector("input#field_login")).sendKeys("edit_test");
        this.driver.findElement(By.cssSelector("input#field_password")).sendKeys("edit_testPass");
        this.driver.findElement(By.cssSelector("button[type=submit]")).click();

        // Sleep to give the page time to populate
        Thread.sleep(1000);

        // Now we search for our entry in the table of results
        boolean found = false;
        for (WebElement row : this.driver.findElements(By.cssSelector("tr"))) {
            List<WebElement> children = row.findElements(By.cssSelector("td"));
            if (children.size() < 3) {
                continue;
            }
            WebElement id = children.get(0);
            WebElement site = children.get(1);
            WebElement login = children.get(2);
            if (site.getText().equals("edit_test.site") && login.getText().equals("edit_test")) {
                // once our entry is found we click the edit button and change
                // the login to 'edit_foo'
                String iid = id.getText();
                this.driver.findElement(By.cssSelector("button[href=\'#/acme-pass/" + iid + "/edit\']")).click();
                WebElement edit_login = this.driver.findElement(By.cssSelector("input#field_login"));
                edit_login.clear();
                edit_login.sendKeys("edit_foo");
                this.driver.findElement(By.cssSelector("button[type=submit]")).click();
                found = true;
                break;
            }
        }
        if (!found) {
            Assert.fail("Failed to find newly created password");
        }
        // Sleep to give the page time to populate
        Thread.sleep(1000);
        // Now we try and find our updated entry in the table
        for (WebElement row : this.driver.findElements(By.cssSelector("tr"))) {
            List<WebElement> children = row.findElements(By.cssSelector("td"));
            if (children.size() < 3) {
                continue;
            }
            WebElement id = children.get(0);
            WebElement site = children.get(1);
            WebElement login = children.get(2);
            if (site.getText().equals("edit_test.site") && login.getText().equals("edit_foo")) {
                // We've found it so test passed, now delete it again
                String iid = id.getText();
                this.driver.findElement(By.cssSelector("button[href=\'#/acme-pass/" + iid + "/delete\']")).click();
                this.driver.findElement(By.cssSelector("button[type=submit]")).click();
                return;
            }
        }
        Assert.fail("Failed to find edited entry");
    }

    @After
    public void finish() {
        this.driver.close();
    }

}
