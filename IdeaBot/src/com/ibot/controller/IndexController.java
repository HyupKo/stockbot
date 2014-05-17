package com.ibot.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.common.util.TwitterUtil;

/**
 * <pre>
 * com.ibot.controller
 *   ㄴIndexController.java
 * </pre>
 * @Author 	Hyup, Ko
 * @Date	2011. 10. 8.
 * @Version	:
 */

@Controller("indexController")
public class IndexController {
	
	@RequestMapping(value="/index/")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		Twitter twitter = (Twitter) request.getSession().getAttribute("_twitter");
		if(twitter == null) {
			TwitterUtil.moveToTwitter(request, response);
		} else {
			Paging paging = new Paging(1);
			try {
				List<Status> list = (List<Status>) twitter.getHomeTimeline(paging);
				request.setAttribute("_TWITTER", list);
			} catch (TwitterException e) {
				System.out.println("[IndexController  -  index]  :  TwitterException");
				e.printStackTrace();
			}
		}
		return "index/index";
	}
}
