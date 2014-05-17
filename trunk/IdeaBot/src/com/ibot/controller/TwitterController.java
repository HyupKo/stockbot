package com.ibot.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.util.TwitterUtil;

/**
 * <pre>
 * com.ibot.controller
 *   ㄴTwitterController.java
 * </pre>
 * @Author 	Hyup, Ko
 * @Date	2011. 10. 7.
 * @Version	:
 */
@Controller("twitterController")
public class TwitterController {
	public TwitterController() {}
	
	@RequestMapping(value="/auth/*")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		try {
			TwitterUtil.setTwitterInstance(request, response);
			response.sendRedirect("/form/");
		} catch (IOException e) {
			System.out.println("[TwitterController  -  index]  :  IOException");
			e.printStackTrace();
		}
		return "";
	}
}

