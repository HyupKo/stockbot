package com.istock.bot.serviceImpl;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.istock.bot.common.OAuthBasic;
import com.istock.bot.service.IndexService;

/**
 * Index Service Implement.
 * @author hyupko
 */
@Service
public class IndexServiceImpl implements IndexService {
	
	/**
	 * Get Simple Information.
	 * @return {@link Map}
	 * @see com.istock.bot.service.IndexService#getInfomation()
	 */
	@Override
	public Map<String, String> getInfomation() {
		return null;
	}

	/**
	 * Get OAuth Url.
	 * @return {@link String}
	 * @see com.istock.bot.service.IndexService#getOAuthUrl()
	 */
	@Override
	public String getOAuthUrl() {
		return OAuthBasic.getAuthUrl();
	}

	/**
	 * Set Instance.
	 * @see com.istock.bot.service.IndexService#setInstance(HttpSession session, String oauthToken, String oauthVerifier)
	 */
	@Override
	public void setInstance(HttpSession session, String oauthToken, String oauthVerifier) {
		OAuthBasic.setInstance(session, oauthToken, oauthVerifier);
	}

	/**
	 * Send Msg.
	 * @param msg
	 */
	@Override
	public void sendMsg(String msg) {
		OAuthBasic.sendMsg(msg);
	}

}
