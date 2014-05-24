package com.istock.bot.scheduler;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.istock.bot.common.OAuthBasic;

/**
 * Parser Scheduler for parsing HTML.
 * @author hyupko
 */
@Component
public class ParserScheduler {
	
	// Set Article Id for daily setting.
	private static String articleId = "2473";
	private int comment_now = 0;
	
	/**
	 * parse page.
	 */
	@Scheduled(fixedRate=60 * 1000)
	private void parsePage() {
		Connection.Response res;
		Map<String, String> map = null;
		
		try {
			res = Jsoup
					.connect("https://logins.daum.net/accounts/login.do")
					.data("id", "istockbot", "pw", "qlalfqjsgh1!")
					.method(Method.POST).execute();
			res.parse();
			map = res.cookies();
			System.out.println(map.toString());
		
			Document document = Jsoup.connect("http://cafe.daum.net/_c21_/shortcomment_read?grpid=17uHu&mgrpid=&fldid=GqjP&dataid=" + articleId + "&icontype=").cookies(map).get();
			System.out.println(document.toString());
			Elements elements = document.select(".comment_contents");
			
			if(comment_now != elements.size()) {
				comment_now = elements.size();
				
				OAuthBasic.sendMsg("댓글 내용 : " + elements.last().text());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set Article Id.
	 * @param id
	 */
	public void setArticleId(String id) {
		articleId = id;
	}
	
	/**
	 * Send Msg.
	 */
	@Scheduled(cron="0 0 1 * * 2-6")
	public void resetCommentNum() {
		comment_now = 0;
	}
}
