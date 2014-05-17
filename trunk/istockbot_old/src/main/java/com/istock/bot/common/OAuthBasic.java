package com.istock.bot.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Component;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

/**
 * OAuth Class.
 * @author Administrator
 *
 */
@Component
public class OAuthBasic {

	final static String REQUEST_TOKEN_URL = "https://apis.daum.net/oauth/requestToken";
	final static String ACCESS_TOKEN_URL = "https://apis.daum.net/oauth/accessToken";
	final static String AUTHORIZE_URL = "https://apis.daum.net/oauth/authorize";

	final static String CONSUMER_KEY = "3a7fb43d-67bf-414b-866b-af915eb4cc41";
	final static String CONSUMER_SECRET = "i9hdtYG6zFQ-mk-fhn_qK2G4UBx25yY.9XglmbgJ-TGAkqff_pvJJw00";

	final static String API_URL = "https://apis.daum.net";

	// Service Provider ��ü ����
	static OAuthProvider provider = new DefaultOAuthProvider(REQUEST_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZE_URL);

	// Consumer ��ü ����
	static OAuthConsumer consumer = new DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

	/**
	 * Get Auth Url.
	 * @return {@link String}
	 */
	public static String getAuthUrl() {
		String authUrl = "";
		
		try {
			authUrl = provider.retrieveRequestToken(consumer, "http://58.231.51.195:8080/oauth");
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		}
		
		return authUrl;
	}
	
	/**
	 * Set oauth instance.
	 * @param oauthToken
	 * @param oauthVerifier
	 */
	public static void setInstance(String oauthToken, String oauthVerifier) {
		try {
			provider.retrieveAccessToken(consumer, oauthVerifier);
			
			final String ACCESS_TOKEN = consumer.getToken();
			final String TOKEN_SECRET = consumer.getTokenSecret();
			
			consumer.setTokenWithSecret(ACCESS_TOKEN, TOKEN_SECRET);
			URL url = new URL(API_URL + "/cafe/favorite_cafes.json");		
			HttpURLConnection request = (HttpURLConnection) url.openConnection();

			// oauth_signature ���� ����ϴ�.
			consumer.sign(request);		

			request.connect();

			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String tmpStr = "";
			while( (tmpStr = br.readLine()) != null) {
				System.out.println(tmpStr);
			}
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
