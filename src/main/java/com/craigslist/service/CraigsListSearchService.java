package com.craigslist.service;

/**
 * Service interface
 * @author Chandra
 *
 */

public interface CraigsListSearchService {
	
	public void saveSearch(String searchName) throws Exception;
	public boolean login() throws Exception;
	public boolean verifySavedSearch(String savedSearch) throws Exception;
	public boolean verifyEditSearch(String savedSearch, String newString) throws Exception;
	public boolean deleteSearchItem(String deleteParam) throws Exception;

}
