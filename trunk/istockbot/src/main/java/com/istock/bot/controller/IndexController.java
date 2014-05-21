package com.istock.bot.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	 * @param session 
	 * @return {@link String}
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Cookie cookie =  new Cookie("HM_CU", "475eV75eV");
		response.addCookie(cookie);
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
		
		indexService.setInstance(oauthToken, oauthVerifier);
		
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
		
		if(action.equals("addBuddy")) {
			System.out.println(request.getParameter("groupId"));
		} else if(action.equals("sendFromMessage")) {
			System.out.println(request.getParameter("groupId"));
		} else if(action.equals("sendFromGroup")) {
			System.out.println(request.getParameter("groupId"));
		} else if(action.equals("createGroup")) {
			System.out.println(request.getParameter("groupId"));
		}
		return "index";
	}

}
