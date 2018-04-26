package com.craigslist.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:craigslist.properties")
@Component
public class CraigsListProperties {
	
	@Value("${clurl}")
	private String clurl;
	
	@Value("${userId}")
	private String userId;
	
	@Value("${pass}")
	private String pass;
	public String getClurl() {
		return clurl;
	}
	public void setClurl(String clurl) {
		this.clurl = clurl;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	

}