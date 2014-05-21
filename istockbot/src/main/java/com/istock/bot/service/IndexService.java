package com.istock.bot.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

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
	 * @param session 
	 * @param oauthToken
	 * @param oauthVerifier
	 */
	public void setInstance(HttpSession session, String oauthToken, String oauthVerifier);

	/**
	 * Send Msg.
	 * @param msg
	 */
	public void sendMsg(String msg);

}
