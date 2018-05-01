package com.craigslist.search.test;

import static com.craigslist.utils.InputParams.ACCEPT;
import static com.craigslist.utils.InputParams.ACCEPT_ENCODING;
import static com.craigslist.utils.InputParams.CONNECTION;
import static com.craigslist.utils.InputParams.CONTENT_TYPE;
import static com.craigslist.utils.InputParams.COOKIE_URL;
import static com.craigslist.utils.InputParams.CSRF_URL;
import static com.craigslist.utils.InputParams.FIND_ALL_IT_CLASS_RESULT_TITLE_HDRLNK;
import static com.craigslist.utils.InputParams.FIND_IT_CLASS_TOTALCOUNT;
import static com.craigslist.utils.InputParams.HOST;
import static com.craigslist.utils.InputParams.INPUT_EMAIL_HANDLE;
import static com.craigslist.utils.InputParams.INPUT_PASSWORD;
import static com.craigslist.utils.InputParams.LOCATION;
import static com.craigslist.utils.InputParams.LOGIN_HOME;
import static com.craigslist.utils.InputParams.REFERER;
import static com.craigslist.utils.InputParams.SAVE_SEARCH;
import static com.craigslist.utils.InputParams.SEARCH_URL;
import static com.craigslist.utils.InputParams.SEARCH_URL_SECOND_PAGE;
import static com.craigslist.utils.InputParams.URL_LOGIN;
import static com.craigslist.utils.InputParams.WHICH_FOR;
import static com.craigslist.utils.InputParams.FIND_ALL_SAVED_SEARCH_LIST;
import static com.craigslist.utils.InputParams.PAGECOUNT;
import static io.restassured.RestAssured.given;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.craigslist.utils.UtilityLibrary;

/**
 * Class to test CraigsList Search API
 * 
 * @author
 *
 */
@RunWith(SpringRunner.class)
public class TestCraigsListSearchAPI {

	public static final int _120 = 120;
	private static final Logger logger = Logger.getLogger(TestCraigsListSearchAPI.class);
	private static Cookies allCookies;
	private static Map<String, String> cookieMap = new HashMap<>();

	@BeforeTest
	public void init() {
		// get required cookies to login
		Map<String, String> cokiesinit = given().when().get(COOKIE_URL).then().statusCode(200)
				.extract().cookies();
		cookieMap.putAll(cokiesinit); 
	}

	@Test
	public void testLogin() {
		logger.info("Verifying login");
		Map<String, String> loginCookies = given()
				.header("Accept-Encoding", ACCEPT_ENCODING)
				.header("Content-Type", CONTENT_TYPE)
				.header("Connection", CONNECTION).header("Accept", ACCEPT)
				.header("Host", HOST).header("Referer", REFERER)
				.formParam("inputEmailHandle", INPUT_EMAIL_HANDLE)
				.formParam("inputPassword", INPUT_PASSWORD)
				.formParam("whichForm", WHICH_FOR).post(URL_LOGIN).then()
				.assertThat().statusCode(302).extract().cookies();
		
		//setting cookies into Map for future requests
		cookieMap.putAll(loginCookies);
		loginCookies.forEach((key,value) -> System.out.println("key: "+key+":: value"+value ));
		
		
		List<Cookie> listCookie = loginCookies.entrySet().stream().map((entry)->{return new Cookie.Builder(entry.getKey(), entry.getValue()).build();})
		.collect(Collectors.toList());
		allCookies = new Cookies(listCookie);
		
		//login to craigslist
		boolean login = given().cookies(allCookies)
		.header("Accept-Encoding", ACCEPT_ENCODING)
		.header("Content-Type", CONTENT_TYPE)
		.header("Connection", CONNECTION).header("Accept", ACCEPT)
		.header("Host", HOST).header("Referer", REFERER)
		.formParam("inputEmailHandle", INPUT_EMAIL_HANDLE)
		.formParam("inputPassword", INPUT_PASSWORD)
		.formParam("whichForm", WHICH_FOR).post(URL_LOGIN).then()
		.assertThat().statusCode(302).extract().header(LOCATION).contains(LOGIN_HOME);
		Assert.assertTrue(login);
	}
	
	@Test(dependsOnMethods = { "testLogin" })
	@Parameters("savedSearch")
	public void testSearch(String savedSearch) {
		logger.info("Verifying CraigsList Search and printing search titles "+savedSearch);
		
		// #TODO replace search urls search parameter to take it from command prompt
		XmlPath html = given().when().get(SEARCH_URL.replace("SEARCH", savedSearch)).then().assertThat()
				.statusCode(200).extract().htmlPath();
		String totalCount = html.get(FIND_IT_CLASS_TOTALCOUNT);
		List<Object> results = new ArrayList<>();

		//get second page results if search results has second page
		if (totalCount != null && Integer.valueOf(totalCount) > PAGECOUNT) {
			results = given().when().get(SEARCH_URL_SECOND_PAGE.replace("SEARCH", savedSearch)).then()
					.assertThat().statusCode(200).extract().htmlPath()
					.getList(FIND_ALL_IT_CLASS_RESULT_TITLE_HDRLNK);
		} else {
			results = html.get(FIND_ALL_IT_CLASS_RESULT_TITLE_HDRLNK);
		}
		
		if (results.isEmpty()) {
			logger.info("Search Results are empty");
		} else {
			results.forEach(System.out::println);
		}
		
		//generate craigslist's Cross-Site Request Forgery (CSRF) 
		String csrf = UtilityLibrary.generateCSRF(CSRF_URL, allCookies);
		logger.info("CSRF::"+csrf);
		String url = SAVE_SEARCH.replace("CSRF", csrf).replace("SEARCH", savedSearch);
		Response resp = given()
				.header("Accept", ACCEPT)
				.header("Accept-Encoding", ACCEPT_ENCODING)
				.header("Connection", CONNECTION).header("Host", HOST)
				.cookies(allCookies).when()
				.get(url).then().assertThat().statusCode(200).extract()
				.response();
		boolean isSaved = resp.htmlPath().get(FIND_ALL_SAVED_SEARCH_LIST).toString().contains(savedSearch);
		
		Assert.assertTrue(isSaved);  
	}


}
