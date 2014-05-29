package com.istock.bot.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.istock.bot.scheduler.ParserScheduler;
import com.istock.bot.service.IndexService;

/**
 * @author hyupko
 */
@Controller
public class IndexController {
	
	@Autowired private IndexService indexService;
	
	/**
	 * index page.
	 * @param request
	 * @param response
	 * @return {@link String}
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response) {
		if(request.getSession().getAttribute("ACCESS_TOKEN") == null)
			request.setAttribute("oauthUrl", indexService.getOAuthUrl());
		return "index";
	}
	
	/**
	 * Oauth.
	 * @param request
	 * @param response
	 * @return {@link String}
	 */
	@RequestMapping(value="/oauth", method=RequestMethod.GET)
	public String oauth(HttpServletRequest request, HttpServletResponse response) {
		String oauthToken = request.getParameter("oauth_token").toString();
		String oauthVerifier = request.getParameter("oauth_verifier").toString();
		
		HttpSession session = request.getSession();
		session.setAttribute("oauthToken", oauthToken);
		session.setAttribute("oauthverifier", oauthVerifier);
		
		indexService.setInstance(session, oauthToken, oauthVerifier);
		
		return "redirect:/";
	}
	
	/**
	 * Callback URL.
	 * @param request
	 * @param response
	 * @return <code>null</code>
	 */
	@RequestMapping(value="/callback")
	public String callback(HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter("action");
		System.out.println(action);
		if(action.equals("addBuddy")) {
			System.out.println(request.getParameter("groupId"));
		} else if(action.equals("sendFromMessage")) {
			System.out.println(request.getParameter("groupId"));
		} else if(action.equals("sendFromGroup")) {
			System.out.println(request.getParameter("groupId"));
			String content = request.getParameter("content");
			if(content.contains("글번호:")) {
				ParserScheduler ps = new ParserScheduler();
				ps.setArticleId(content.split(":")[1]);
				indexService.sendMsg(content + " 변경.");
			}
			if(content.contains("세팅")){
				ParserScheduler ps = new ParserScheduler();
				ps.printTodayEvent();
			}
		} else if(action.equals("createGroup")) {
			System.out.println(request.getParameter("groupId"));
		}
		return null;
	}
	
	/**
	 * Send Message.
	 * @param request
	 * @param response
	 * @param msg 
	 */
	@RequestMapping(value="/msg/{msg}")
	public void msg(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String msg) {
		indexService.sendMsg(msg);
	}

}
