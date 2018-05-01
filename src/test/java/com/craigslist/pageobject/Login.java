package com.craigslist.pageobject;

import static com.craigslist.utils.Locators.ACCOUNTFORM_BTN;
import static com.craigslist.utils.Locators.INPUT_EMAIL_HANDLE;
import static com.craigslist.utils.Locators.INPUT_PASSWORD;
import static com.craigslist.utils.Locators.LOG_OUT;
import static com.craigslist.utils.Locators.MY_ACCOUNT;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import com.craigslist.properties.CraigsListProperties;
import com.craigslist.utils.UtilityLibrary;

public class Login {
	
	@Autowired
	CraigsListProperties craigProps;
	private static final Logger logger = Logger.getLogger(Login.class);
	
	public boolean login() throws Exception{
		logger.info("Trying to Login");
		boolean status = false;
		try{
			UtilityLibrary.driver.get(craigProps.getClurl());	
			UtilityLibrary.driver.manage().window().maximize();
			UtilityLibrary.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			//click on my account to login
			UtilityLibrary.driver.findElement(By.linkText(MY_ACCOUNT)).click();		
			UtilityLibrary.driver.findElement(By.name(INPUT_EMAIL_HANDLE)).sendKeys(craigProps.getUserId());
			UtilityLibrary.driver.findElement(By.name(INPUT_PASSWORD)).sendKeys(craigProps.getPass());
			UtilityLibrary.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			UtilityLibrary.driver.findElements(By.className(ACCOUNTFORM_BTN)).get(0).click();

			//check if login successfull
			WebElement webelement = UtilityLibrary.driver.findElement(By.linkText(LOG_OUT));
			if(webelement.getText().contains(LOG_OUT)){
				logger.info("Login successfull");
				status = true;
			}
		}catch(NoSuchElementException ex){
			UtilityLibrary.driver.close();
			logger.error("Page element not found"+ex.getMessage());
			status = false;
		}
		return status;
	}

}
