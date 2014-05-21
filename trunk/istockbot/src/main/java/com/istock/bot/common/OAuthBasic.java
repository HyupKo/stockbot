package com.istock.bot.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpSession;

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

	static final String REQUEST_TOKEN_URL = "https://apis.daum.net/oauth/requestToken";
	static final String ACCESS_TOKEN_URL = "https://apis.daum.net/oauth/accessToken";
	static final String AUTHORIZE_URL = "https://apis.daum.net/oauth/authorize";

	static final String CONSUMER_KEY = "41d2e064-a0a8-4d7e-bde4-08d152b91ccb";
	static final String CONSUMER_SECRET = "yUuPS9bO465Qo_lN78fPJDbDBhflXTBnjetTc8KZRRDYufz6ObHm.g00";

	static final String API_URL = "https://apis.daum.net";
	static final String MYPEOPLE_URL = "https://apis.daum.net/mypeople/group/send.json";
	
	static final String APIKEY = "28fe9374603e307ba29a1c4934c5532b8265706e";

	// Service Provider 생성.
	static OAuthProvider provider = new DefaultOAuthProvider(REQUEST_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZE_URL);

	// Consumer 생성.
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
	 * @param session 
	 * @param oauthToken
	 * @param oauthVerifier
	 */
	public static void setInstance(HttpSession session, String oauthToken, String oauthVerifier) {
		try {
			String accessToken = consumer.getToken();
			String tokenSecret = consumer.getTokenSecret();
			
			System.out.println(accessToken + "   " + tokenSecret + "   " + oauthToken + "   " + oauthVerifier);
			
			session.setAttribute("ACCESS_TOKEN", accessToken);
			session.setAttribute("TOKEN_SECRET", tokenSecret);
			
			provider.retrieveAccessToken(consumer, oauthVerifier);
			
			consumer.setTokenWithSecret(accessToken, tokenSecret);
			URL url = new URL(API_URL + "/cafe/favorite_cafes.json");		
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			
			// oauth_signature 받아오기.
			consumer.sign(request);
			request.connect();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String tmpStr = "";
			while( (tmpStr = br.readLine()) != null) {
				System.out.println("return  :  " + tmpStr);
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
	
	/**
	 * Send msg.
	 * @param msg
	 */
	public static void sendMsg(String msg) {
		String requestMsg = "groupId=GID_x6Qd2&content=" + msg + "&apikey=" + APIKEY;
		URL url;
		OutputStream outputStream = null;
		try {
			url = new URL(MYPEOPLE_URL);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.setRequestMethod("POST");
			request.setUseCaches(false);
			request.setDoOutput(true);
			
			outputStream = request.getOutputStream();
			outputStream.write(requestMsg.getBytes("UTF-8"));
			outputStream.flush();
			
			request.connect();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String tmpStr = "";
			
			while ((tmpStr = br.readLine()) != null) {
				System.out.println(tmpStr);
				
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
