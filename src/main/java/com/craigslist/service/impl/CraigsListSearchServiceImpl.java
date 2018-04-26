package com.craigslist.service.impl;

import static com.craigslist.utils.Locators.ACCOUNTFORM_BTN;
import static com.craigslist.utils.Locators.CLASS_RESULT_TITLE_HDRLNK;
import static com.craigslist.utils.Locators.CRAIGSLIST;
import static com.craigslist.utils.Locators.ID_SAVEDSEARCHLIST;
import static com.craigslist.utils.Locators.ID_SAVEDSEARCHLIST_TBODY;
import static com.craigslist.utils.Locators.ID_SAVEDSEARCHLIST_TBODY_TR_X_TD_6_A;
import static com.craigslist.utils.Locators.ID_SAVEDSEARCHLIST_TBODY_TR_X_TD_7_A;
import static com.craigslist.utils.Locators.INPUT_EMAIL_HANDLE;
import static com.craigslist.utils.Locators.INPUT_PASSWORD;
import static com.craigslist.utils.Locators.LOG_OUT;
import static com.craigslist.utils.Locators.LOG_OUT2;
import static com.craigslist.utils.Locators.MY_ACCOUNT;
import static com.craigslist.utils.Locators.NEXT;
import static com.craigslist.utils.Locators.QUERY;
import static com.craigslist.utils.Locators.SAVE_SEARCH;
import static com.craigslist.utils.Locators.SEARCHES;
import static com.craigslist.utils.Locators.SUB_NAME;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.Assert;

import com.craigslist.properties.CraigsListProperties;
import com.craigslist.service.CraigsListSearchService;
import com.craigslist.utils.UtilityLibrary;

@Service
public class CraigsListSearchServiceImpl implements CraigsListSearchService {


	@Autowired
	CraigsListProperties craigProps;

	private static final Logger logger = Logger.getLogger(CraigsListSearchServiceImpl.class);

	/**
	 * Method to login to craigs list 
	 * @return boolean
	 */	
	@SuppressWarnings("finally")
	@Override
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
		}catch(Exception ex){
			UtilityLibrary.driver.close();
			status = false;
		}
		return status;


	}


	/**
	 * Method to search, print the result titles and saves the search
	 * @param searchName
	 * 
	 */
	@Override
	public void saveSearch(String searchName) throws Exception {

		Assert.assertTrue(login(),"Login failed...Please retry with  correct credentials");
		//if login successfull navigate to craigslist home page
		UtilityLibrary.driver.findElement(By.linkText(CRAIGSLIST)).click();
		WebElement elements  = UtilityLibrary.driver.findElement(By.name(QUERY));
		//enter the searchkeyword and submit
		elements.sendKeys(searchName);
		elements.submit();
		UtilityLibrary.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//check if "Next" is enables. If print current page results
		if(UtilityLibrary.driver.findElement(By.linkText(NEXT)).isEnabled()){
			UtilityLibrary.driver.findElement(By.linkText(NEXT)).click();
		}
		

		//get all results from page 2 and print 
		List<WebElement> liElements = UtilityLibrary.driver.findElements(By.xpath(CLASS_RESULT_TITLE_HDRLNK));
		for (WebElement webElement : liElements) {
			logger.info(webElement.getText());
		}
		//save the search
		UtilityLibrary.driver.findElement(By.linkText(SAVE_SEARCH)).click();
		UtilityLibrary.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
	}

	/**
	 * Method to verify the search has been saved or not
	 * @param String savedSearch
	 * @return boolean 
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean verifySavedSearch(String savedSearch) throws Exception{
		//get all saved entries
		List<WebElement> savedEntries = UtilityLibrary.driver.findElements(By.xpath(ID_SAVEDSEARCHLIST));
		String xpath =ID_SAVEDSEARCHLIST_TBODY;
		boolean status = false;
		//check if the search item has been saved or not
		for(int i=1; i<=savedEntries.size(); i++){
			WebElement element = UtilityLibrary.driver.findElement(By.xpath(xpath.replace("X", Integer.toString(i))));
			if(element.getText().equalsIgnoreCase(savedSearch)){
				status =  true;
				break;
			}
			
		}
		return status;
	}

	/**
	 * Method to edit and verify the saved search
	 * @param String savedSearch 
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean verifyEditSearch(String savedSearch, String newString) throws Exception{
		boolean status = false;
		String xpath =ID_SAVEDSEARCHLIST_TBODY;
		String xpathEdit = ID_SAVEDSEARCHLIST_TBODY_TR_X_TD_6_A;
		//logout and re-login 
		UtilityLibrary.driver.findElement(By.linkText(LOG_OUT2)).click();
		if(login()){
			//navigate to searches tab
			UtilityLibrary.driver.findElement(By.linkText(SEARCHES)).click();
			//get all the saved entries and locate the search that need to be edited
			List<WebElement> savedEntries = UtilityLibrary.driver.findElements(By.xpath(ID_SAVEDSEARCHLIST));
			for(int i=1; i<=savedEntries.size(); i++){
				WebElement element = UtilityLibrary.driver.findElement(By.xpath(xpath.replace("X", Integer.toString(i))));
				if(element.getText().equalsIgnoreCase(savedSearch)){
					UtilityLibrary.driver.findElement(By.xpath(xpathEdit.replace("X", Integer.toString(i)))).click();
					break;
				}
				
			}
			//edit the search and save
			WebElement ele = UtilityLibrary.driver.findElement(By.name(SUB_NAME));
			ele.clear();
			ele.sendKeys(newString);
			ele.submit();
			UtilityLibrary.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			 UtilityLibrary.driver.navigate().refresh();
			 UtilityLibrary.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			//check if the new value is saved or not
			status = verifySavedSearch(newString);
		}
		return status;
	}

	/**
	 * Method to delete the search and verify the delete
	 * @param String deleteParam
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	public boolean deleteSearchItem(String deleteParam) throws Exception{
		boolean status=false;
		//get saved entries from searches tab
		List<WebElement> savedEntries = UtilityLibrary.driver.findElements(By.xpath(ID_SAVEDSEARCHLIST));
		//find the right element to deelete
		for(int i=1; i<=savedEntries.size(); i++){
			WebElement element = UtilityLibrary.driver.findElement(By.xpath(ID_SAVEDSEARCHLIST_TBODY.replace("X", Integer.toString(i))));
			if(element.getText().equalsIgnoreCase(deleteParam)){
				UtilityLibrary.driver.findElement(By.xpath(ID_SAVEDSEARCHLIST_TBODY_TR_X_TD_7_A.replace("X", Integer.toString(i)))).click();
				break;
			}
			
		}
		UtilityLibrary.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		status = verifySavedSearch(deleteParam);

		return false;
	}


}
