package com.istock.bot.scheduler;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.istock.bot.common.OAuthBasic;

/**
 * Parser Scheduler for parsing HTML.
 * @author hyupko
 */
@Component
public class ParserScheduler {
	@Value("${filter.text}") String rexTxt;
	
	private static boolean activeSchedule = true;
	private static String botId = "istockbot.bot";
	private static String botPw = "qlalfqjsgh1!";
	
	// Set Article Id for daily setting.
	private static String articleId = "";

	private int comment_now = 0;
	private Map <String, String> map = null;
	
	/**
	 * daily morning init function
	 */
	@Scheduled(cron="${bot.initCron}")
	public void init(){
		OAuthBasic.sendMsg("- 봇 초기화 "
				+ "\n- 07:30~08:30 장전거래"
				+ "\n- 09:00~15:00 보통거래"
				+ "\n- 15:10~15:30 장후거래"
				+ "\n- 성투합시다 (도움말 : /?)");
		resetCommentNum();
		setActiveSchedule(true);
		printForCallback();
	}

	/**
	 * parse page.
	 */
	@Scheduled(cron="${bot.cron}")
	private void parsePage() {
		if(activeSchedule){
			try {
				if(map == null || map.isEmpty()) {
					map = loginForCookies();
					printTodayEvent();
				}
				if(articleId.equals("")){
					setActiveSchedule(false);
				}
				else {
					Document document = Jsoup.connect("http://cafe.daum.net/_c21_/shortcomment_read?grpid=17uHu&mgrpid=&fldid=GqjP&dataid=" + articleId + "&icontype=")
											.ignoreHttpErrors(true)
											.cookies(map).get();
					Elements elements = document.select(".comment_contents");
					if(elements.size()>0){
						if(comment_now != elements.size()) {
							comment_now = elements.size();
							Pattern rexPattern = Pattern.compile(rexTxt);
							Matcher matTxt = rexPattern.matcher(elements.last().text());
							if(matTxt.find()){
								OAuthBasic.sendMsg("> " + elements.last().text());
							}
							else {
								OAuthBasic.sendMsg("[임시필터링] " + elements.last().text());
							}
						}
					}
					else {
						OAuthBasic.sendMsg("- 댓글이 존재하지 않습니다.\n- 동작을 중지합니다.");
						setActiveSchedule(false);
					}
				}
			} catch (IOException e) {
				setActiveSchedule(false);
				e.printStackTrace();
			}
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
				.data("id", botId, "pw", botPw)
				.method(Method.POST).execute();
			loginRes.parse();
			sendMap = loginRes.cookies();
			if(sendMap == null || sendMap.isEmpty()){
				setActiveSchedule(false);
			}
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
				String listTitle = freeBoardlist.first().text();
				String listUrl = freeBoardlist.first().parent().parent().select("a[href]").attr("abs:href");
				String listId = freeBoardlist.first().parent().parent().parent().parent().select("td.num").text();
				// set Article Id
				setArticleId(listId);
				// free today's contents info
				Document freeRecCont = Jsoup.connect(listUrl).cookies(map).get();
				Elements freeBoardCont = freeRecCont.select("#template_xmp");
				Document freeBodyTable = Jsoup.parse(freeBoardCont.text());
				Elements freeBodyList = freeBodyTable.select("tbody");
				if(freeBodyList.size()>1){
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
						freeContentsMsg.append(freeInfo.select("td").get(0).text().replaceAll("\u00a0", "").trim());
						freeContentsMsg.append(" / ");
						freeContentsMsg.append(freeInfo.select("td").get(1).text().replaceAll("\u00a0", "").split("-")[0].trim());
					}
					OAuthBasic.sendMsg(freeContentsMsg.toString());
				}
				else {
					OAuthBasic.sendMsg("- 금일 추천종목이 없습니다.\n- 동작을 중지합니다.");
					setArticleId("");
					setActiveSchedule(false);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * IndexController callback msg
	 */
	public void printForCallback(){
		if(activeSchedule){
			if(map == null || map.isEmpty()) {
				map = loginForCookies();
			}
			printTodayEvent();
		}
	}
	
	/**
	 * stock code info search
	 * @param codeNo
	 */
	public void searchCode(String codeNo){
		try {
			Document stockConn = Jsoup
					.connect("http://m.stock.daum.net/item/main.daum?code="+codeNo)
					.ignoreHttpErrors(true)
					.get();
			searchPrint(stockConn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * stock name list search
	 * @param searchWord
	 */
	public void searchName(String searchWord){
		try {
			Response searchBody = Jsoup
				.connect("http://m.stock.daum.net/m/search/search.daum")
				.data("name", searchWord)
				.ignoreHttpErrors(true)
				.method(Method.POST).execute();
			Document searchResult = searchBody.parse();
			if(searchResult.select(".item_idx_info").size()>0){
				searchPrint(searchResult);
			}
			else {
				Elements searchTable = searchResult.select("#resultTable tbody tr");
				if(searchTable.size()>0){
					Iterator<Element> searchTr = searchTable.iterator();
					Element resultTr = null;
					StringBuffer searchResultMsg = new StringBuffer();
					searchResultMsg.append("- 종목명 검색결과 ("+searchTable.size()+") -");
					while(searchTr.hasNext()){
						resultTr = searchTr.next();
						searchResultMsg.append("\n" + resultTr.select("th abbr").attr("title")
								+ " (" + resultTr.select(".inp_check").attr("value") + ")");
					}
					OAuthBasic.sendMsg(searchResultMsg.toString());
				}
				else {
					OAuthBasic.sendMsg("- 검색결과가 없습니다.");
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * stock search common logic
	 * @param searchDoc
	 */
	public void searchPrint(Document searchDoc){
		Elements stockInfo = searchDoc.select(".item_idx_info");
		String stockType = stockInfo.select("h2 .txt_kospi").text();
		String stockName = stockInfo.select("h2 .link_name").text();
		String stockCode = stockInfo.select("h2 .stock_code").text();
		String stockPrice = stockInfo.select(".price").text();
		String stockFluc = stockInfo.select(".price_fluc").text();
		String stockRate = stockInfo.select(".rate_fluc").text();
		
		Elements stockSummary = searchDoc.select(".summary .prices");
		String stockCur = stockSummary.select(".cur").text();
		String stockMax = stockSummary.select(".max").text();
		String stockMin = stockSummary.select(".min").text();
		if(!stockCode.equals("") && stockCode!=null){
			OAuthBasic.sendMsg("["+stockType+"]" + " " + stockName + " ("+stockCode+")"
					+ "\n" + stockPrice+" " + stockFluc + " " + stockRate
					+ "\n시가: " + stockCur
					+ "\n고가: " + stockMax
					+ "\n저가: " + stockMin);
		}
		else {
			OAuthBasic.sendMsg("- 검색결과가 없습니다.");
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
	 * Set ActiveSchedule
	 * @param act
	 */
	public void setActiveSchedule(boolean act) {
		activeSchedule = act;
	}
	
	/**
	 * Send Msg.
	 */
	public void resetCommentNum() {
		comment_now = 0;
	}
}
