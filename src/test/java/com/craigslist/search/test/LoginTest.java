package com.craigslist.search.test;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.craigslist.pageobject.Login;
import com.craigslist.utils.UtilityLibrary;

@RunWith(SpringRunner.class)
public class LoginTest {
	
	private Login login;
	static ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	private static final Logger logger = Logger.getLogger(LoginTest.class);
	
	@BeforeTest
	@Parameters({"browser","savedSearch"})
	public void setUp(String browser,String savedSearch) throws Exception{
		login = (Login)context.getBean("Login");
		UtilityLibrary.openBrowser(browser);
		logger.info("Browser Opened Successfully");
	}
	
	@Test(priority=0)
	public void testLogin() throws Exception{
		logger.info("Trying to login to CraigsList");
		boolean isLogin = login.login();
		if(!isLogin){
			UtilityLibrary.quitBrowser();
			logger.info("Browser Closed Successfully");
		}
		Assert.assertTrue(isLogin, "Login to CraigsList failed");
		logger.info("Login Success");
	}
}
