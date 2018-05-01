package com.craigslist.search.test;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.craigslist.pageobject.Search;
import com.craigslist.utils.UtilityLibrary;

@RunWith(SpringRunner.class)
public class SearchTest {
	
	static ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	private static final Logger logger = Logger.getLogger(SearchTest.class);
	private Search search;
	
	@BeforeTest
	@Parameters({"browser","savedSearch"})
	public void setUp(String browser,String savedSearch) throws Exception{
		search = (Search) context.getBean("Search");
	}
	
	@Test(priority=1)
	@Parameters({"savedSearch","newValue"})
	public void testEditSavedSearch(String savedSearch, String newValue) throws Exception{
		logger.info("Renaming search item in progress......"); 
		Assert.assertTrue(search.verifyEditSearch(savedSearch, newValue), "Newly edited value not found");
		logger.info("Edit  Search Item Verification Completed Successfully with New Value "+ newValue);
	}
	
	@Test(priority=2, dependsOnMethods = { "testEditSavedSearch" })
	@Parameters({"newValue"})
	public void testDeleteSavedSearch(String newValue) throws Exception{
		Assert.assertFalse(search.deleteSearchItem(newValue), "Search item " +newValue+" not deleted");		
		logger.info("Delete Search Item Verification Completed Successfully with New Value "+ newValue);
	}
	
	@AfterTest
	public void teardown(){
		UtilityLibrary.quitBrowser();
		logger.info("Browser Closed Successfully");
	}


}
