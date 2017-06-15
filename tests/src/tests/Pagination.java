package tests;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import util.ServerConfig;

public class Pagination {

	private static String baseUrl = "http://localhost:8080";

	private WebDriver driver;
	
	@BeforeClass
	public static void setUpOnce() throws Exception {
		if (System.getProperty("webdriver.gecko.driver") == null)
			System.setProperty("webdriver.gecko.driver", "bin/geckodriver");

		if (System.getProperty("url") != null)
			baseUrl = System.getProperty("url");
	}
	
	@Before
	public void setUp() throws Exception {
		ServerConfig.Setup(baseUrl, 5);

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
	}
	
	@Test
	public void test() throws Exception {
		driver.get("http://localhost:8080/#/acme-pass");
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		WebElement allpages = driver.findElement(By.xpath("/html/body/div[3]/div/div/ul/li[2]/a"));
		
		WebElement test = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[3]/jhi-item-count/div"));
		
		String amounts = test.getText(); // showing x - y of z items
		
		String[] stringArray = amounts.split(" ");
		
		int totalnumberofrecords = Integer.parseInt(stringArray[5]);
		int recordsperpage = Integer.parseInt(stringArray[3]);
		
		System.out.println(totalnumberofrecords);
		System.out.println(recordsperpage);
	
		
		int clickcount = totalnumberofrecords/recordsperpage;
		
		
		System.out.println(clickcount);

		
		for (int i = 0; i <= clickcount; i++){
			Thread.sleep(2000);
			allpages = driver.findElement(By.xpath("/html/body/div[3]/div/div/ul/li[2]/a"));

				try{	
				allpages.click();
				System.out.println("Pagination enabled");
	
				}
				
				catch (Exception e){
				
					System.out.println(e.toString());
					
					if(i == clickcount){
						
						assertEquals(true, true);
					}
					
					else{
						
					Assert.fail();
					}
					
					System.out.println("Pagination disabled");	
				}		
		}	
		
		
	    allpages = driver.findElement(By.xpath("/html/body/div[3]/div/div/ul/li[1]/a"));
		for (int i = 0; i <= clickcount; i++){
			Thread.sleep(2000);
			allpages = driver.findElement(By.xpath("/html/body/div[3]/div/div/ul/li[1]/a"));
				try{	
				allpages.click();
				System.out.println("Pagination enabled");
	
				}
				
				catch (Exception e){
				
					if(i == clickcount){
						
						assertEquals(true, true);
					}
					
					else{
						
					Assert.fail();
					}
					
					System.out.println("Pagination disabled");	
				}		
		}	
	}

}
