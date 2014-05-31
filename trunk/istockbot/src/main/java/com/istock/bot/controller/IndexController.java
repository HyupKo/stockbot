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
			if(content.contains("/r:")) {
				ParserScheduler ps = new ParserScheduler();
				indexService.sendMsg("- 글번호 " + content.split(":")[1] + " 파싱시작 -");
				ps.resetCommentNum();
				ps.setActiveSchedule(true);
				ps.setArticleId(content.split(":")[1]);
			}
			if(content.contains("/c:")) {
				ParserScheduler ps = new ParserScheduler();
				//indexService.sendMsg("- 종목코드 " + content.split(":")[1] + " 파싱시작 -");
				ps.searchCode(content.split(":")[1]);
			}
			if(content.contains("/n:")) {
				ParserScheduler ps = new ParserScheduler();
				ps.searchName(content.split(":")[1]);
			}
			if(content.equals("/s")){
				ParserScheduler ps = new ParserScheduler();
				indexService.sendMsg("- 최근글로 파싱시작 -");
				ps.resetCommentNum();
				ps.setActiveSchedule(true);
				ps.printForCallback();
			}
			if(content.equals("/a")){
				ParserScheduler ps = new ParserScheduler();
				indexService.sendMsg("- 동작실행 -");
				ps.resetCommentNum();
				ps.setActiveSchedule(true);
			}
			if(content.equals("/q")){
				ParserScheduler ps = new ParserScheduler();
				indexService.sendMsg("- 동작중지 -");
				ps.setActiveSchedule(false);
			}
			if(content.equals("/?")){
				indexService.sendMsg("/? - 도움말"
						+ "\n/s - 최근글로 파싱시작"
						+ "\n/r:글번호 - 글번호 파싱시작"
						+ "\n/c:종목코드 - 종목코드로 조회"
						+ "\n/n:종목단어 - 종목명으로 조회"
						+ "\n/a - 동작실행"
						+ "\n/q - 동작중지");
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
