package com.craigslist.search.test;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.craigslist.service.CraigsListSearchService;
import com.craigslist.utils.UtilityLibrary;

@RunWith(SpringRunner.class)

public class TestCraigsListSearch {
	
	
	private CraigsListSearchService clsService;
	
	static ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	private static final Logger logger = Logger.getLogger(TestCraigsListSearch.class);
	
	@BeforeTest
	@Parameters({"browser","savedSearch"})
	public void setUp(String browser,String savedSearch) throws Exception{
		clsService = (CraigsListSearchService) context.getBean("CraigsListSearchServiceImpl");
		UtilityLibrary.openBrowser(browser);
		logger.info("Browser Opened Successfully");
		clsService.saveSearch(savedSearch);
		logger.info("Serach Item Saved - "+ savedSearch);
	}
	
		
	@Test(priority=0)
	@Parameters("savedSearch")
	
	public void testSaveSearch(String savedSearch) throws Exception{
		Assert.assertTrue(clsService.verifySavedSearch(savedSearch), "Search with value "+savedSearch+" not found");
		logger.info("Saved Search Item Verification Completed Successfully with Value "+ savedSearch);
	}
	
	@Test(priority=1)
	@Parameters({"savedSearch","newValue"})
	public void testEditSavedSearch(String savedSearch, String newValue) throws Exception{
		Assert.assertTrue(clsService.verifyEditSearch(savedSearch, newValue), "Newly edited value not found");
		logger.info("Edit  Search Item Verification Completed Successfully with New Value "+ newValue);
	}
	
	@Test(priority=3)
	@Parameters({"newValue"})
	public void testDeleteSavedSearch(String newValue) throws Exception{
		Assert.assertFalse(clsService.deleteSearchItem(newValue), "Search item " +newValue+" not deleted");		
		logger.info("Delete Search Item Verification Completed Successfully with New Value "+ newValue);
	}
	
	@AfterTest
	public void teardown(){
		
		UtilityLibrary.quitBrowser();
		logger.info("Browser Closed Successfully");
	}
	
		

}
