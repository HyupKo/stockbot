package com.ibot.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ibot.model.TwitterDmSendModel;
import com.ibot.service.FormService;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * <pre>
 * com.ibot.controller
 *   ㄴFormController.java
 * </pre>
 * @Author 	고협
 * @Date	2011. 10. 27.
 * @Version	1.0.0
 */
@Controller("formController")
public class FormController {
	
	@Autowired FormService formService;

	@RequestMapping(value="/form/", method=RequestMethod.GET)
	public String form(HttpServletRequest request) {
		Twitter twitter = (Twitter) request.getSession().getAttribute("_twitter");
		try {
			request.setAttribute("USER_NM", twitter.getScreenName());
		} catch (IllegalStateException e) {
			System.out.println("[FormController  -  form]  :  IllegalStateException");
			e.printStackTrace();
		} catch (TwitterException e) {
			System.out.println("[FormController  -  form]  :  TwitterException");
			e.printStackTrace();
		}
		return "/form/index";
	}
	
	@RequestMapping(value="/form/regist/", method=RequestMethod.POST)
	public String form_regist(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="krCode", required=true) String krCode,
			@RequestParam(value="activateTime", required=true) String activateTime) {
		Twitter twitter = (Twitter) request.getSession().getAttribute("_twitter");
		try {
			String twitterId = twitter.getScreenName();
			TwitterDmSendModel model = new TwitterDmSendModel();
			model.setTwitterId(twitterId);
			model.setKrCode(krCode);
			model.setActivateTime(activateTime);
			
			formService.createUserStockData(model);
		} catch (IllegalStateException e) {
			System.out.println("[FormController  -  form_regist]  :  IllegalStateException");
			e.printStackTrace();
		} catch (TwitterException e) {
			System.out.println("[FormController  -  form_regist]  :  TwitterException");
			e.printStackTrace();
		}
		return "/form/regist";
	}
}

