package com.craigslist.pageobject;

import static com.craigslist.utils.Locators.CLASS_RESULT_TITLE_HDRLNK;
import static com.craigslist.utils.Locators.CRAIGSLIST;
import static com.craigslist.utils.Locators.ID_SAVEDSEARCHLIST;
import static com.craigslist.utils.Locators.ID_SAVEDSEARCHLIST_TBODY;
import static com.craigslist.utils.Locators.ID_SAVEDSEARCHLIST_TBODY_TR_X_TD_6_A;
import static com.craigslist.utils.Locators.ID_SAVEDSEARCHLIST_TBODY_TR_X_TD_7_A;
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
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import com.craigslist.properties.CraigsListProperties;
import com.craigslist.service.impl.CraigsListSearchServiceImpl;
import com.craigslist.utils.UtilityLibrary;

public class Search {
	
	@Autowired
	CraigsListProperties craigProps;
	Login login;

	private static final Logger logger = Logger.getLogger(CraigsListSearchServiceImpl.class);
	
	
	/**
	 * Method to search, print the result titles and saves the search
	 * @param searchName
	 * 
	 */
	public void saveSearch(String searchName) throws Exception {

		Assert.assertTrue(login.login(),"Login failed...Please retry with  correct credentials");
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
	public boolean verifyEditSearch(String savedSearch, String newString) throws Exception{
		boolean status = false;
		try{
			String xpath =ID_SAVEDSEARCHLIST_TBODY;
			String xpathEdit = ID_SAVEDSEARCHLIST_TBODY_TR_X_TD_6_A;
			//logout and re-login 
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
		}catch(NoSuchElementException ex){
			logger.error("Not able to find the web element "+ex.getMessage());
		}catch(Exception ex){
			logger.error(ex.getMessage());
		}
		return status;
	}

	/**
	 * Method to delete the search and verify the delete
	 * @param String deleteParam
	 * @return boolean
	 */
	public boolean deleteSearchItem(String deleteParam) throws Exception{
		boolean status=false;
		try{
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
		}catch(NoSuchElementException ex){
			logger.error("Not able to find the web element "+ex.getMessage());
		}catch(Exception ex){
			logger.error(ex.getMessage());
		}
		
		return status;
	}

}
