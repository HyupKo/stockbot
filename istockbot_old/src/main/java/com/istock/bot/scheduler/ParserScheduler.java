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

/**
 * Parser Scheduler for parsing HTML.
 * @author hyupko
 */
@Component
public class ParserScheduler {
	
	// Set Article Id for daily setting.
	private static String articleId = "2473";
	
	/**
	 * parse page.
	 */
	@Scheduled(fixedRate=5000)
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
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			Document document = Jsoup.connect("http://cafe.daum.net/_c21_/shortcomment_read?grpid=17uHu&mgrpid=&fldid=GqjP&dataid=" + articleId + "&icontype=").cookies(map).get();
			System.out.println(document.toString());
			Elements elements = document.select("#commentDiv-" + articleId);
			System.out.println(elements.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set Article Id.
	 * @param articleId
	 */
	public void setArticleId(String articleId) {
		ParserScheduler.articleId = articleId;
	}

}
