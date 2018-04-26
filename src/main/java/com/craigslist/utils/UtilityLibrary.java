package com.craigslist.utils;


import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class UtilityLibrary {

	public static WebDriver driver;
	public static void openBrowser(String browserName) throws IOException{
		
		if(browserName.equals("chrome")){
			Resource resource = new ClassPathResource("chromedriver.exe");
			System.setProperty("webdriver.chrome.driver",  resource.getURI().getPath());
			driver = new ChromeDriver();	
			
		}
		//#TODO add support for other browsers
		//Similarly we can code for all other required browsers here
		else{
			System.err.println("Invalid driver");
		}
	}
	
	public static void quitBrowser(){
		driver.quit();		
	}

	public static  void closeBrowser(){
		driver.close();
	}


}
