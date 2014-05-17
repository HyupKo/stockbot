package com.istockbot.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import twitter4j.Twitter;
import twitter4j.User;

import com.common.util.TwitterUtil;
import com.istockbot.model.TwitterDmSendModel;
import com.istockbot.service.IndexDwrService;

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
	@Autowired IndexDwrService indexDwrService;
	@RequestMapping(value="/index/")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		Twitter twitter = (Twitter) request.getSession().getAttribute("_twitter");
		if(twitter == null) {
			TwitterUtil.moveToTwitter(request, response);
		} else {
			User user = null;
			String userInfo = "";
			try {
				user = twitter.showUser(twitter.getScreenName());
				userInfo += "<img src='"+user.getProfileImageURL()+"' width=20 height=20 align=absmiddle>";
				userInfo += " "+user.getName();
				userInfo += "("+user.getScreenName()+")";
				request.getSession().setAttribute("_userId", user.getScreenName());
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			request.setAttribute("_TWITTINFO", userInfo);
			
			String list = indexDwrService.getTwitterTimeLine(1);
			request.setAttribute("_TWITTER", list);
			
			String inda = indexDwrService.getParseIndexInfo();
			request.setAttribute("_INDEXINFO", inda);
			
			TwitterDmSendModel model = new TwitterDmSendModel();
			model.setTwitterId(user.getScreenName());
			List<TwitterDmSendModel> info_list = indexDwrService.getInfoList(model);
			String defaultCode = "005930";
			if(!info_list.isEmpty()){
				defaultCode = info_list.get(0).getkrCode();
			}
			request.setAttribute("_INFO_LIST",info_list);
			request.setAttribute("_DEFAULT_KRCODE",defaultCode);
			
			String data = indexDwrService.getParseStockInfo(defaultCode);
			request.setAttribute("_STOCKINFO", data);
		}
		return "index/index";
	}
}
