package tests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import util.WebDriverFactory;
import junit.framework.TestCase;

public class Sort extends TestCase{
	
	private WebDriver driver;
	
	@Before
	public void setUp() throws Exception{
		
		System.setProperty("webdriver.chrome.driver", "C:/Users/Crims/Documents/chromedriver_win32/chromedriver.exe");

		driver = WebDriverFactory.Create();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		
	}
	
	public void testLogin() throws Exception{
		
		this.driver.get("http://localhost:8080");
		
		WebElement login = driver.findElement(By.id("login"));
		login.click();
		
		WebElement username = driver.findElement(By.id("username"));
		
		WebElement password = driver.findElement(By.id("password"));
		
		username.sendKeys("jo.thomas@acme.com");
		
		password.sendKeys("12345");
		
		WebElement submit = driver.findElement(By.id("login"));
		
		
		submit.click();	
		
	}
	
	
	
	public void testID() throws Exception{
		
		testLogin();
		
		Thread.sleep(2000);
		
		driver.get("http://localhost:8080/index.html#/acme-pass");
		
		List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[1]"));
		
		int[] array = new int[data.size()];
		
		for (int i = 0; i < data.size(); i++){
		System.out.println(data.get(i).getText());
		array[i] = Integer.parseInt(data.get(i).getText());
		
		}
		
		int[] sortedArray = new int[data.size()];
		
		sortedArray = array;
		
		Arrays.sort(sortedArray);
		
		assertTrue(sortedArray.equals(array));
			
	}
	
	public void testIDRev() throws Exception{
		
		testLogin();
		
		Thread.sleep(2000);
		
		driver.get("http://localhost:8080/index.html#/acme-pass?sort=id,desc");
		
		List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[1]"));
		
		int[] array = new int[data.size()];
		
		for (int i = 0; i < data.size(); i++){
		System.out.println(data.get(i).getText());
		array[i] = Integer.parseInt(data.get(i).getText());
		
		}
		
		int[] sortedArray = new int[data.size()];
		
		sortedArray = array;
		
		Arrays.sort(sortedArray);
		
		ArrayUtils.reverse(sortedArray);
		
		assertTrue(sortedArray.equals(array));
			
	}
	
public void testSite() throws Exception{
		
		testLogin();
		
		Thread.sleep(2000);
		
		driver.get("http://localhost:8080/index.html#/acme-pass?sort=site,asc");
		
		List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[2]"));
		
		String[] array = new String[data.size()];
		
		for (int i = 0; i < data.size(); i++){
		System.out.println(data.get(i).getText());
		array[i] = data.get(i).getText();
		
		}
		
        String[] sortedArray = new String[data.size()];
		
		sortedArray = array;
		
		Arrays.sort(sortedArray);
		
		//ArrayUtils.reverse(sortedArray);
		
		assertTrue(sortedArray.equals(array));

			
	}
public void testSiteRev() throws Exception{
	
	testLogin();
	
	Thread.sleep(2000);
	
	driver.get("http://localhost:8080/index.html#/acme-pass?sort=site,desc");
	
	List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[2]"));
	
	String[] array = new String[data.size()];
	
	for (int i = 0; i < data.size(); i++){
	System.out.println(data.get(i).getText());
	array[i] = data.get(i).getText();
	
	}
	
    String[] sortedArray = new String[data.size()];
	
	sortedArray = array;
	
	Arrays.sort(sortedArray);
	
	ArrayUtils.reverse(sortedArray);
	
	assertTrue(sortedArray.equals(array));

		
}

public void testLoginSort() throws Exception{
	
	testLogin();
	
	Thread.sleep(2000);
	
	driver.get("http://localhost:8080/index.html#/acme-pass?sort=login,asc");
	
	List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[3]"));
	
	String[] array = new String[data.size()];
	
	for (int i = 0; i < data.size(); i++){
	System.out.println(data.get(i).getText());
	array[i] = data.get(i).getText();
	
	}
	
    String[] sortedArray = new String[data.size()];
	
	sortedArray = array;
	
	Arrays.sort(sortedArray);
	
	//ArrayUtils.reverse(sortedArray);
	
	assertTrue(sortedArray.equals(array));

		
}
public void testLoginSortRev() throws Exception{
	
	testLogin();
	
	Thread.sleep(2000);
	
	driver.get("http://localhost:8080/index.html#/acme-pass?sort=login,desc");
	
	List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[3]"));
	
	String[] array = new String[data.size()];
	
	for (int i = 0; i < data.size(); i++){
	System.out.println(data.get(i).getText());
	array[i] = data.get(i).getText();
	
	}
	
    String[] sortedArray = new String[data.size()];
	
	sortedArray = array;
	
	Arrays.sort(sortedArray);
	
	ArrayUtils.reverse(sortedArray);
	
	assertTrue(sortedArray.equals(array));

		
}
public void testCreateDate() throws Exception{
	
	testLogin();
	
	Thread.sleep(2000);
	
	DateFormat formatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss a");
	
	System.out.println((Date)formatter.parse("Jun 14, 2017 5:06:15 PM"));
	
	

	
	
	driver.get("http://localhost:8080/index.html#/acme-pass?sort=createdDate,asc");
	
	List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[5]"));
	
	Date[] arrayOfDates = new Date[data.size()];
	
	for (int i = 0; i < data.size(); i++){
	System.out.println(data.get(i).getText());
	arrayOfDates[i] = (Date)formatter.parse(data.get(i).getText());
	
	}
	
    Date[] sortedArray = new Date[data.size()];
	
	sortedArray = arrayOfDates;
	
	Arrays.sort(sortedArray);
	
	//ArrayUtils.reverse(sortedArray);
	
	assertTrue(sortedArray.equals(arrayOfDates));

		
}
public void testCreateDateRev() throws Exception{
	
	testLogin();
	
	Thread.sleep(2000);
	
	DateFormat formatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss a");
	
	System.out.println((Date)formatter.parse("Jun 14, 2017 5:06:15 PM"));
	
	

	
	
	driver.get("http://localhost:8080/index.html#/acme-pass?sort=createdDate,desc");
	
	List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[5]"));
	
	Date[] arrayOfDates = new Date[data.size()];
	
	for (int i = 0; i < data.size(); i++){
	System.out.println(data.get(i).getText());
	arrayOfDates[i] = (Date)formatter.parse(data.get(i).getText());
	
	}
	
    Date[] sortedArray = new Date[data.size()];
	
	sortedArray = arrayOfDates;
	
	Arrays.sort(sortedArray);
	
	ArrayUtils.reverse(sortedArray);
	
	assertTrue(sortedArray.equals(arrayOfDates));

		
}
public void testModifiedDate() throws Exception{
	
	testLogin();
	
	Thread.sleep(2000);
	
	DateFormat formatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss a");
	
	driver.get("http://localhost:8080/index.html#/acme-pass?sort=lastModifiedDate,asc");
	
	List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[6]"));
	
	Date[] arrayOfDates = new Date[data.size()];
	
	for (int i = 0; i < data.size(); i++){
	System.out.println(data.get(i).getText());
	arrayOfDates[i] = (Date)formatter.parse(data.get(i).getText());
	
	}
	
    Date[] sortedArray = new Date[data.size()];
	
	sortedArray = arrayOfDates;
	
	Arrays.sort(sortedArray);
	
	//ArrayUtils.reverse(sortedArray);
	
	assertTrue(sortedArray.equals(arrayOfDates));

		
}

public void testModifiedDateRev() throws Exception{
	
	testLogin();
	
	Thread.sleep(2000);
	
	DateFormat formatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss a");
	
	driver.get("http://localhost:8080/index.html#/acme-pass?sort=lastModifiedDate,desc");
	
	List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[6]"));
	
	Date[] arrayOfDates = new Date[data.size()];
	
	for (int i = 0; i < data.size(); i++){
	System.out.println(data.get(i).getText());
	arrayOfDates[i] = (Date)formatter.parse(data.get(i).getText());
	
	}
	
    Date[] sortedArray = new Date[data.size()];
	
	sortedArray = arrayOfDates;
	
	Arrays.sort(sortedArray);
	
	ArrayUtils.reverse(sortedArray);
	
	assertTrue(sortedArray.equals(arrayOfDates));

		
}
	
	@After
	public void tearDown() throws Exception {
		this.driver.quit();
	}
	
}