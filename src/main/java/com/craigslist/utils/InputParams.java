package com.craigslist.utils;

public interface InputParams {
	
	public static final String ACCEPT_ENCODING = "gzip, deflate, br";
	public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
	public static final String CONNECTION = "keep-alive";
	public static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
	public static final String HOST = "accounts.craigslist.org";
	public static final String REFERER = "https://accounts.craigslist.org/login?rt=L&rp=/login/home";
	public static final String INPUT_EMAIL_HANDLE = "justforchandu@gmail.com";
	public static final String INPUT_PASSWORD = "@$Dec123";
	public static final String WHICH_FOR = "login";
	public static final String URL_LOGIN = "https://accounts.craigslist.org/login";
	public static final String COOKIE_URL = "https://accounts.craigslist.org/login/home";
	public static final String FIND_ALL_IT_CLASS_RESULT_TITLE_HDRLNK = "**.findAll { it.@class == 'result-title hdrlnk' }";
	public static final String FIND_ALL_SAVED_SEARCH_LIST = "**.findAll { it.@id == 'savedsearchlist' }";
	
	public static final String FIND_IT_CLASS_TOTALCOUNT = "**.find{ it.@class == 'totalcount' }";
	public static final String SEARCH_URL_SECOND_PAGE = "https://sfbay.craigslist.org/search/sss?s=120&query=SEARCH&sort=rel";
	public static final String SEARCH_URL = "https://sfbay.craigslist.org/search/sss?query=SEARCH&sort=rel";
	public static final String LOGIN_HOME = "/login/home";
	public static final String LOCATION = "Location";
	public static final String SAVE_SEARCH = "https://accounts.craigslist.org/savesearch/save?URL=https://sfbay.craigslist.org/search/sss?query=SEARCH&sort=rel&_csrf=CSRF";
	public static final String FIND_ALL_SAVED_SEARCHES = "**.findAll { it.@id == 'savedsearchlist' }";
	public static final String CSRF_URL="https://accounts.craigslist.org/session/?v=1";
	public static final int PAGECOUNT=120;
	
	
	
	
	
	

}
