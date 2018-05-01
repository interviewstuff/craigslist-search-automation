package com.craigslist.utils;


import static io.restassured.RestAssured.given;
import io.restassured.http.Cookies;

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
	
	
	public static String generateCSRF(String url, Cookies allCookies){
		String csrf = given()
				.header("Accept", "text/javascript, */*; q=0.01")
				.header("X-Requested-With", "XMLHttpRequest")
				.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
				.cookies(allCookies)
				.when()
				.get("https://accounts.craigslist.org/session/?v=1").jsonPath().get("csrf");
		return csrf;
	}
	
	public static void quitBrowser(){
		driver.quit();		
	}

	public static  void closeBrowser(){
		driver.close();
	}


}
