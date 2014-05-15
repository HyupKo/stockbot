package com.istock.bot.serviceImpl;

import java.util.Map;

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

	@Override
	public String getOAuthUrl() {
		return OAuthBasic.getAuthUrl();
	}

	@Override
	public void setInstance(String oauthToken, String oauthVerifier) {
		OAuthBasic.setInstance(oauthToken, oauthVerifier);
	}

}
