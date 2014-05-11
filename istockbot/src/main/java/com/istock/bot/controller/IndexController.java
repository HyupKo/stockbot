package com.istock.bot.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author hyupko
 */
@Controller
public class IndexController {
	
	/**
	 * index page.
	 * @param request
	 * @param response
	 * @return {@link String}
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return "index";
	}

}
