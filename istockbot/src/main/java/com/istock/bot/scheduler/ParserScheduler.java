package com.istock.bot.scheduler;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
	private static String articleId = "";
	
	// daily list info
	private static String listTitle = "";
	private static String listUrl = "";
	private static String listId = "";
	// daily contents info
	
	private int comment_now = 0;
	private Map <String, String> map = null;
	
	/**
	 * parse page.
	 */
	@Scheduled(cron="0/3 * * * * 2-6")
	private void parsePage() {
		
		try {
			if(map == null || map.isEmpty()) {
				//System.out.println("============================\n" + res.body());
				map = loginForCookies();
				printTodayEvent();
				// System.out.println(res.statusCode() + "\n" + map.toString());
			}
		
			Document document = Jsoup.connect("http://cafe.daum.net/_c21_/shortcomment_read?grpid=17uHu&mgrpid=&fldid=GqjP&dataid=" + articleId + "&icontype=").cookies(map).get();
			// System.out.println(document.toString());
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
	 * make Cookie
	 * @return sendMap
	 */
	public Map <String, String> loginForCookies(){
		Response loginRes;
		Map <String, String> sendMap = null;
		try {
			loginRes = Jsoup
				.connect("https://logins.daum.net/accounts/login.do")
				.data("id", "istockbot.bot", "pw", "qlalfqjsgh1!")
				.method(Method.POST).execute();
			loginRes.parse();
			sendMap = loginRes.cookies();
		} catch (IOException e){
			e.printStackTrace();
		}
		return sendMap;
	}
	
	/**
	 * recently recommand event msg send
	 */
	public void printTodayEvent(){
		StringBuffer freeContentsMsg = new StringBuffer();
		try{
			if(map != null && !map.isEmpty()) {
				// free list main page
				Document freeRecBoard = Jsoup.connect("http://cafe.daum.net/_c21_/bbs_list?grpid=17uHu&fldid=GqjP").cookies(map).get();
				// free today's list info
				Elements freeBoardlist = freeRecBoard.select("font[color=#9934C4]");
				listTitle = freeBoardlist.first().text();
				listUrl = freeBoardlist.first().parent().parent().select("a[href]").attr("abs:href");
				listId = freeBoardlist.first().parent().parent().parent().parent().select("td.num").text();
				// set Article Id
				setArticleId(listId);
				// free today's contents info
				Document freeRecCont = Jsoup.connect(listUrl).cookies(map).get();
				Elements freeBoardCont = freeRecCont.select("#template_xmp");
				Document freeBodyTable = Jsoup.parse(freeBoardCont.text());
				Elements freeBodyList = freeBodyTable.select("tbody");
				Element freeBody = freeBodyList.get(2);
				Iterator<Element> freeTable = freeBody.select("tr").iterator();
				freeTable.next();
				freeTable.next();
				Element freeInfo = null;
				// make free event msg
				freeContentsMsg.append(listTitle);
				while(freeTable.hasNext()){
					freeInfo = freeTable.next();
					freeContentsMsg.append("\n");
					freeContentsMsg.append(freeInfo.select("td").get(0).text().replaceAll("&nbsp;", "").trim());
					freeContentsMsg.append(" / ");
					freeContentsMsg.append(freeInfo.select("td").get(1).text().replaceAll("&nbsp;", "").split("-")[0].trim());
				}
				OAuthBasic.sendMsg(freeContentsMsg.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * IndexController callback msg
	 */
	public void printForCallback(){
		map = loginForCookies();
		printTodayEvent();
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
