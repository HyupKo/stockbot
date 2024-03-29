package com.istock.bot.service;

import java.util.Map;

/**
 * Index Service.
 * @author hyupko
 */
public interface IndexService {
	
	/**
	 * Get Simple Information.
	 * @return {@link Map}
	 */
	public Map<String, String> getInfomation();
	
	/**
	 * Get OAuth Url.
	 * @return {@link String}
	 */
	public String getOAuthUrl();

	/**
	 * Set oauth instance.
	 * 
	 * @param oauthToken
	 * @param oauthVerifier
	 */
	public void setInstance(String oauthToken, String oauthVerifier);

}
