package util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverFactory {
	public static WebDriver Create() {
		String driverType = System.getProperty("test.driver");
		
		if (System.getProperty("webdriver.gecko.driver") == null)
			System.setProperty("webdriver.gecko.driver", "bin/geckodriver");
		
		if (driverType == "firefox")
			return new FirefoxDriver();
		else if (driverType == "chrome")
			return new ChromeDriver();
					
		return new FirefoxDriver();
	}
}
