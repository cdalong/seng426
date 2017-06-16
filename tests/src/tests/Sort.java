package tests;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import util.ServerConfig;
import util.WebDriverFactory;

public class Sort {
	
	private static WebDriver driver;
	private static WebDriverWait wait;
	private static String baseUrl = "http://localhost:8080";

	@BeforeClass
	public static void setUpOnce() throws Exception {
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
	
	@AfterClass
	public static void tearDownOnce() throws Exception {
		driver.quit();
	}

	@Test	
	public void testID() throws Exception {
		
		driver.get(baseUrl + "/#/acme-pass");
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.table-responsive")));
		
		List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[1]"));
		
		int[] array = new int[data.size()];
		for (int i = 0; i < data.size(); i++)
			array[i] = Integer.parseInt(data.get(i).getText());
		
		int[] sortedArray = array.clone();
		Arrays.sort(sortedArray);
		
		assertTrue(Arrays.equals(array, sortedArray));
	}
	
	@Test
	public void testIDRev() throws Exception {
		
		driver.get(baseUrl + "/#/acme-pass");
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.table-responsive")));
		
		String xpath = "//th[@jh-sort-by='id']";
		driver.findElement(By.xpath(xpath)).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath(xpath + "/span[2]"), "class", "glyphicon-sort-by-attributes"));

		List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[1]"));
		
		int[] array = new int[data.size()];
		for (int i = 0; i < data.size(); i++)
			array[i] = Integer.parseInt(data.get(i).getText());
		
		int[] sortedArray = array.clone();
		Arrays.sort(sortedArray);
		ArrayUtils.reverse(sortedArray);
		
		assertTrue(Arrays.equals(array, sortedArray));
	}
	
	@Test
	public void testSite() throws Exception {
		
		driver.get(baseUrl + "/#/acme-pass");
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.table-responsive")));
			
		String xpath = "//th[@jh-sort-by='site']";
		driver.findElement(By.xpath(xpath)).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath(xpath + "/span[2]"), "class", "glyphicon-sort-by-attributes-alt"));
		
		List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[2]"));
		
		String[] array = new String[data.size()];
		for (int i = 0; i < data.size(); i++)
			array[i] = data.get(i).getText();
		
        String[] sortedArray = array.clone();
		Arrays.sort(sortedArray);
		
		assertTrue(Arrays.equals(array, sortedArray));
	}
	
	@Test
	public void testSiteRev() throws Exception{
		
		driver.get(baseUrl + "/#/acme-pass");
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.table-responsive")));
		
		String xpath = "//th[@jh-sort-by='site']";
		driver.findElement(By.xpath(xpath)).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath(xpath + "/span[2]"), "class", "glyphicon-sort-by-attributes-alt"));
		driver.findElement(By.xpath(xpath)).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath(xpath + "/span[2]"), "class", "glyphicon-sort-by-attributes"));
		
		List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[2]"));
		
		String[] array = new String[data.size()];
		
		for (int i = 0; i < data.size(); i++)
		array[i] = data.get(i).getText();
		
	    String[] sortedArray = array.clone();
		Arrays.sort(sortedArray);
		ArrayUtils.reverse(sortedArray);
		
		assertTrue(Arrays.equals(array, sortedArray));
	}
	
	@Test
	public void testLoginSort() throws Exception{
		
		driver.get(baseUrl + "/#/acme-pass");
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.table-responsive")));
			
		String xpath = "//th[@jh-sort-by='login']";
		driver.findElement(By.xpath(xpath)).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath(xpath + "/span[2]"), "class", "glyphicon-sort-by-attributes-alt"));
		
		List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[3]"));
		
		String[] array = new String[data.size()];
		
		for (int i = 0; i < data.size(); i++)
			array[i] = data.get(i).getText();
		
	    String[] sortedArray = array.clone();
		Arrays.sort(sortedArray);

		assertTrue(Arrays.equals(array, sortedArray));
	}
	
	@Test
	public void testLoginSortRev() throws Exception{
		
		driver.get(baseUrl + "/#/acme-pass");
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.table-responsive")));	
		
		String xpath = "//th[@jh-sort-by='login']";
		driver.findElement(By.xpath(xpath)).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath(xpath + "/span[2]"), "class", "glyphicon-sort-by-attributes-alt"));
		driver.findElement(By.xpath(xpath)).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath(xpath + "/span[2]"), "class", "glyphicon-sort-by-attributes"));
		
		List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[3]"));
		
		String[] array = new String[data.size()];
		for (int i = 0; i < data.size(); i++)
			array[i] = data.get(i).getText();
		
	    String[] sortedArray = array.clone();
		Arrays.sort(sortedArray);
		ArrayUtils.reverse(sortedArray);
		
		assertTrue(Arrays.equals(array, sortedArray));
	}
	
	@Test
	public void testCreateDate() throws Exception{

		driver.get(baseUrl + "/#/acme-pass");
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.table-responsive")));	
		
		String xpath = "//th[@jh-sort-by='createdDate']";
		driver.findElement(By.xpath(xpath)).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath(xpath + "/span[2]"), "class", "glyphicon-sort-by-attributes-alt"));
		
		List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[5]"));
		
		DateFormat formatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss a");
		Date[] arrayOfDates = new Date[data.size()];
		for (int i = 0; i < data.size(); i++)
			arrayOfDates[i] = (Date)formatter.parse(data.get(i).getText());
		
	    Date[] sortedArray = arrayOfDates.clone();
		Arrays.sort(sortedArray);
		
		assertTrue(Arrays.equals(arrayOfDates, sortedArray));
	}
	
	@Test
	public void testCreateDateRev() throws Exception{
		
		driver.get(baseUrl + "/#/acme-pass");
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.table-responsive")));	
		
		String xpath = "//th[@jh-sort-by='createdDate']";
		driver.findElement(By.xpath(xpath)).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath(xpath + "/span[2]"), "class", "glyphicon-sort-by-attributes-alt"));
		driver.findElement(By.xpath(xpath)).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath(xpath + "/span[2]"), "class", "glyphicon-sort-by-attributes"));
		
		List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[5]"));
		
		DateFormat formatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss a");
		Date[] arrayOfDates = new Date[data.size()];
		for (int i = 0; i < data.size(); i++)
			arrayOfDates[i] = (Date)formatter.parse(data.get(i).getText());
		
	    Date[] sortedArray = arrayOfDates.clone();
		Arrays.sort(sortedArray);
		ArrayUtils.reverse(sortedArray);
		
		assertTrue(Arrays.equals(arrayOfDates, sortedArray));
	}
	
	@Test
	public void testModifiedDate() throws Exception{
		
		driver.get(baseUrl + "/#/acme-pass");
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.table-responsive")));	
		
		String xpath = "//th[@jh-sort-by='lastModifiedDate']";
		driver.findElement(By.xpath(xpath)).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath(xpath + "/span[2]"), "class", "glyphicon-sort-by-attributes-alt"));
		
		List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[6]"));
		
		DateFormat formatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss a");
		Date[] arrayOfDates = new Date[data.size()];
		
		for (int i = 0; i < data.size(); i++)
			arrayOfDates[i] = (Date)formatter.parse(data.get(i).getText());
		
	    Date[] sortedArray = arrayOfDates.clone();
		Arrays.sort(sortedArray);
		
		assertTrue(Arrays.equals(arrayOfDates, sortedArray));
	}

	@Test
	public void testModifiedDateRev() throws Exception{
		
		driver.get(baseUrl + "/#/acme-pass");
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.table-responsive")));	
		
		String xpath = "//th[@jh-sort-by='lastModifiedDate']";
		driver.findElement(By.xpath(xpath)).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath(xpath + "/span[2]"), "class", "glyphicon-sort-by-attributes-alt"));
		driver.findElement(By.xpath(xpath)).click();
		wait.until(ExpectedConditions.attributeContains(By.xpath(xpath + "/span[2]"), "class", "glyphicon-sort-by-attributes"));
		
		List<WebElement> data = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/table/tbody/tr/td[6]"));
		
		DateFormat formatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss a");
		Date[] arrayOfDates = new Date[data.size()];
		
		for (int i = 0; i < data.size(); i++)
			arrayOfDates[i] = (Date)formatter.parse(data.get(i).getText());
		
	    Date[] sortedArray = arrayOfDates.clone();
		Arrays.sort(sortedArray);
		ArrayUtils.reverse(sortedArray);
		
		assertTrue(Arrays.equals(arrayOfDates, sortedArray));
	}
}