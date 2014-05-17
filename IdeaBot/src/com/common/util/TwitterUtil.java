package com.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibot.model.TwitterDmSendModel;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * <pre>
 * com.common.util
 *   ㄴTwitterUtil.java
 * </pre>
 * @Author 	Hyup, Ko
 * @Date	2011. 10. 7.
 * @Version	:
 */

public class TwitterUtil {
	public TwitterUtil() {}
	
	/**
	 * Twitter 인증을 위해 이동하는 Method
	 * 
	 * @param request
	 * @param response
	 * @see
	 */
	public static void moveToTwitter(HttpServletRequest request, HttpServletResponse response) {
		final String CONSUMER_KEY = "KSvtc7GkNZvCHp5IamUGkA"; 						//APP등록후받은consumer key
		final String CONSUMER_SECRET = "vyO9BGz80lOR14bGI2KgYS38BVsupxej8ys6745Q"; 	//APP등록후받은consumer secret
		
		//트위터인스턴스생성
		Twitter twitter = new TwitterFactory().getInstance();
		request.getSession().setAttribute("_twitter", twitter);
		//인증요청토큰생성
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		RequestToken requestToken = null;
		try {
			requestToken = twitter.getOAuthRequestToken();
			String tokenSecret = requestToken.getTokenSecret();
			String token = requestToken.getToken();

			//CallBack페이지에서이용하기위하여토큰비밀번호를세션에저장한다.
			request.getSession().setAttribute("REQUEST_TOKEN", token);
			request.getSession().setAttribute("REQUEST_TOKEN_SECRET", tokenSecret);
			
			request.getSession().setAttribute("CONSUMER_KEY", CONSUMER_KEY);
			request.getSession().setAttribute("CONSUMER_SECRET", CONSUMER_SECRET);

			response.sendRedirect(requestToken.getAuthenticationURL());
		} catch (TwitterException e) {
			System.out.println("[TwitterUtil  -  moveToTwitter]  :  TwitterException");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("[TwitterUtil  -  moveToTwitter]  :  Exception");
			e.printStackTrace();
		}
	}
	
	/**
	 * Twitter 에서 받아온 값으로 AccessToken을 만드는 Method.
	 * 
	 * @param request
	 * @param response
	 * @see
	 */
	public static void setTwitterInstance(HttpServletRequest request, HttpServletResponse response) {
		Twitter twitter = (Twitter) request.getSession().getAttribute("_twitter");
		AccessToken acsToken = null;
		try {
			acsToken = twitter.getOAuthAccessToken();
			request.getSession().setAttribute("ACCESS_TOKEN", acsToken);
			request.getSession().setAttribute("_twitter", twitter);
			request.getSession().setAttribute("_user", twitter.showUser(twitter.getScreenName()));
		} catch (TwitterException e) {
			System.out.println("[TwitterUtil  -  setTwitterInstance]  :  TwitterException");
			e.printStackTrace();
		}
	}
	
	public static void sendDirectMessage(TwitterDmSendModel model) {
		Twitter twitter = new TwitterFactory().getInstance();
		String[] krCodeArr = model.getKrCode().split(":");
		String stockdata = "";
		
		for(int i=0 ; i<krCodeArr.length ; i++) {
			stockdata += ParserUtil.getStockInfoDM(krCodeArr[i]);
			if(i != krCodeArr.length-1) {
				stockdata += "\n";
			}
		}
				
		try {
			twitter.sendDirectMessage(model.getTwitterId(), stockdata);
		} catch (TwitterException e) {
			System.out.println("[TwitterUtil  -  sendDirectMessage]  :  TwitterException");
			e.printStackTrace();
		}
	}
}

