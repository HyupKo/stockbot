package com.common.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.istockbot.model.TwitterDmSendModel;


public class ParserUtil {
	public ParserUtil(){}
	
	/**
	 * url의 내용을 읽어 BufferedReader로 리턴
	 * 
	 * @param 	site url
	 * @return	BufferedReader
	 * @see
	 */
	public static BufferedReader parsingSite(String site) throws IOException{
		String addr = new String(site);
		URL url = new URL(addr);
		URLConnection connection = url.openConnection();
		InputStream is = (InputStream) connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		return br;
	}
	
	/**
	 * 주요 지수(인덱스) 파싱 후 List<TwitterDmSendModel> 타입으로 리턴
	 * 
	 * @param 	
	 * @return	List<TwitterDmSendModel>
	 * @see
	 */
	public static List<TwitterDmSendModel> getIndexInfo(){
		List<TwitterDmSendModel> TDlist = new ArrayList<TwitterDmSendModel>();
		String buf = null;
		String str = "";
		try{
			BufferedReader br = parsingSite("http://stock.daum.net");
			while((buf = br.readLine())!=null){
				if(buf.matches(".*internalStock.*")){
					while((buf = br.readLine().trim())!=null){
						if(buf.matches(".*chart_kospi.*")){
							break;
						}
						TwitterDmSendModel model = new TwitterDmSendModel();
						buf = buf.replaceAll("<[^>]*>"," ");
						StringTokenizer stk = new StringTokenizer(buf," ");
						int tk=0;
						while(stk.hasMoreTokens()){
							if(tk==0) model.setIndexName(stk.nextToken());
							else if(tk==1) model.setIndexPrice(stk.nextToken());
							else if(tk==2) model.setIndexCompPrice(stk.nextToken());
							else if(tk==3) model.setIndexCompPercent(stk.nextToken());
							tk++;
						}
						if(!buf.trim().equals("")){
							TDlist.add(model);
						}
					}
					break;
				}
			}
		}
		catch(Exception e){
			System.out.println("[ParserUtil  -  getIndexInfo]  :  Exception");
			e.printStackTrace();
		}
		return TDlist;
	}
	
	/**
	 * 주요 지수(인덱스) 파싱 후 String 타입으로 리턴
	 * 
	 * @param 	
	 * @return	String
	 * @see
	 */
	public static String getIndexInfoStr(){
		List<TwitterDmSendModel> model = getIndexInfo();
		String retValue = "";
		for(int i=0; i<model.size(); i++){
			retValue += model.get(i).getIndexName()+"&nbsp;&nbsp;&nbsp;&nbsp;";
			retValue += model.get(i).getIndexPrice()+"  ";
			retValue += model.get(i).getIndexCompPrice()+"  ";
			retValue += model.get(i).getIndexCompPercent()+"<br>";
		}
		return retValue;
	}
	/**
	 * 종목코드를 입력받아 파싱 후 결과를 맵 형식으로 리턴
	 * 
	 * @param 	code 
	 * @return	Map<String, String>
	 * @see
	 */
	public static Map<String, String> getStockInfo(String code) throws IOException{
		String buf = null;
		int sc2=0;
		BufferedReader br = parsingSite("http://stock.daum.net/item/main.daum?code="+code);
		Map<String,String> map = new HashMap<String,String>();

		while((buf = br.readLine())!=null){
			if(buf.matches(".*topNotice.*")){
				int sc1=0;
				while((buf = br.readLine().trim())!=null){
					if(buf.matches(".*sprites.*")){
						break;
					}
					buf = buf.replaceAll("<[^>]*>","");
					if(!buf.trim().equals("")){
						if(sc1==0){
							map.put("closeInfo",buf);
							map.put("isClose","off");
						}
						sc1++;
					}
				}
			}
			if(buf.matches(".*CT_ZONE_DETAILTOPINFO.*")){
				while((buf = br.readLine().trim())!=null){
					if(buf.matches(".*askedPrice.*")){
						break;
					}
					buf = buf.replaceAll("<[^>]*>","");
					if(!buf.trim().equals("")){
						if(sc2==0){
							map.put("stockName",buf);
						}
						else if(sc2==1){
							map.put("krCode",buf);
						}
						else if(sc2==2){
							map.put("nowPrice",buf);
						}
						else if(sc2==3){
							map.put("compPrice",buf);
						}
						else if(sc2==4){
							map.put("compPercent",buf);
						}
						sc2++;
					}
				}
			}
			if(buf.matches(".*stockContent.*")){
				sc2++;
				while((buf = br.readLine().trim())!=null){
					if(buf.matches(".*upjongNTheme.*")){
						break;
					}
					buf = buf.replaceAll("<[^>]*>","");
					if(!buf.trim().equals("")){
						if(sc2==6){
							map.put("nameCode",buf);
						}else if(sc2==8){
							map.put("yesterday",buf);
						}else if(sc2==10){
							map.put("maxPrice",buf);
						}else if(sc2==12){
							map.put("volume",buf);
						}else if(sc2==14){
							map.put("marketValue",buf);
						}else if(sc2==16){
							map.put("minPrice",buf);
						}else if(sc2==18){
							map.put("marketAllValue",buf);
						}else if(sc2==20){
							map.put("highPrice",buf);
						}else if(sc2==22){
							map.put("week52Max",buf);
						}else if(sc2==24){
							map.put("foreignerRate",buf);
						}else if(sc2==26){
							map.put("lowPrice",buf);
						}else if(sc2==28){
							map.put("week52Min",buf);
						}else if(sc2==30){
							map.put("epsPer",buf);
						}
						sc2++;
					}
				}
				break;
			}
		}
		if(sc2!=0){
			map.put("isClose","on");
			map.put("closeInfo","");
			if(map.get("compPercent").contains("+"))map.put("compPrice","▲"+map.get("compPrice"));
			else if(map.get("compPercent").contains("-"))map.put("compPrice","▽"+map.get("compPrice"));
		}
		return map;
	}
	
	/**
	 * 종목코드를 입력받아 맵맵타입의 결과를 String 타입으로 리턴
	 * 
	 * @param 	code 
	 * @return	String
	 * @see
	 */
	public static String getStockInfoStr(String code, TwitterDmSendModel model){
		String sendData = "";
		NumberFormat nf = NumberFormat.getNumberInstance();
		DecimalFormat df = new DecimalFormat("######0.00"); 
		try{
			Map<String, String> data = ParserUtil.getStockInfo(code);
			if(data.get("isClose").equals("on")) {
				sendData = data.get("nameCode") + "<br>현재: " + data.get("nowPrice") + " " + data.get("compPrice") + "(" + data.get("compPercent") + ")";
				sendData += "<br>전일: ";
				sendData += data.get("yesterday");		// 전일대비.<br>전일대비: ";
				sendData += "&nbsp;&nbsp;&nbsp;&nbsp;시가: ";
				sendData += data.get("marketValue");	// 시가.
				sendData += "<br>고가: ";
				sendData += data.get("highPrice");		// 고가.
				sendData += "&nbsp;&nbsp;&nbsp;&nbsp;저가: ";
				sendData += data.get("lowPrice");		// 저가.
				sendData += "<br>상한가: ";
				sendData += data.get("maxPrice");		// 상한가.
				sendData += "&nbsp;&nbsp;&nbsp;&nbsp;하한가: ";
				sendData += data.get("minPrice");		// 하안가.
				sendData += "<br>52주 최고가: ";
				sendData += data.get("week52Max");		// 52주 최고가.
				sendData += "&nbsp;&nbsp;&nbsp;&nbsp;52주 최저가: ";
				sendData += data.get("week52Min");		// 52주 최저가.
				sendData += "<br>거래량: ";
				sendData += data.get("volume");		// 거래량.
				sendData += "<br>시가총액: ";
				sendData += data.get("marketAllValue");		// 시가총액.
				sendData += "<br>외인비율: ";
				sendData += data.get("foreignerRate");		// 외인비율.
				sendData += "<br>EPS/PER: ";
				sendData += data.get("epsPer");		// EPS/PER.
				
				int myPrice = model.getStPrice();
				int myQuantity = model.getStQuantity();
				int getPrice = myPrice*myQuantity;
				int setPrice = Integer.parseInt(data.get("nowPrice").replaceAll(",", ""))*myQuantity;
				int pmPrice = setPrice-getPrice;
				String holdInfo = "<br><br>보유정보";
				if(myPrice>0&&myQuantity>0){
					double ratio=(double)pmPrice/(double)getPrice*100;
					holdInfo += "<br>보유수량 : "+nf.format(myQuantity)+"<br>평균매입가 : "+nf.format(myPrice)+"원";
					holdInfo += "<br>원금 : "+nf.format(getPrice)+"원";
					holdInfo += "<br>평가손익 : "+nf.format(pmPrice) +"원";
					holdInfo += "<br>추정예탁 : "+nf.format(setPrice)+"원";
					holdInfo += "<br>수익률 : "+df.format(ratio)+"%";
					sendData += holdInfo;
				}
				else {
					sendData += holdInfo+"<br>보유정보가 없습니다.";
				}
				
			} else if(data.get("isClose").equals("off")) {
				sendData = data.get("closeInfo");
			}
		}
		catch(Exception e){
			System.out.println("[ParserUtil  -  getStockInfoStr]  :  Exception");
			e.printStackTrace();
		}
		return sendData;
	}
	
	/**
	 * 종목코드를 입력받아 DM용 결과를 String 타입으로 리턴
	 * ibot에서 사용, istockbot에는 미사용
	 * 
	 * @param 	code 
	 * @return	String
	 * @see
	 */
	public static String getStockInfoDM(String code){
		String sendData = "";
		try{
			Map<String, String> data = ParserUtil.getStockInfo(code);
			if(data.get("isClose").equals("on")) {
				sendData += data.get("nameCode") + "\n현재: " + data.get("nowPrice") + "\n전일: " + data.get("yesterday") + "\n전일대비: ";
				sendData += data.get("compPrice");
				sendData += " (" + data.get("compPercent") + ") ";
				sendData += "\n시가: ";
				sendData += data.get("marketValue");	// 시가.
				sendData += "\n고가: ";
				sendData += data.get("highPrice");		// 고가.
				sendData += "\n저가: ";
				sendData += data.get("lowPrice");		// 저가.
				sendData += "\n상한가: ";
				sendData += data.get("maxPrice");		// 상한가.
				sendData += "\n하한가: ";
				sendData += data.get("minPrice");		// 하한가.
			} else if(data.get("isClose").equals("off")) {
				sendData = data.get("closeInfo");
			}
		}
		catch(Exception e){
			System.out.println("[ParserUtil  -  getStockInfoStr]  :  Exception");
			e.printStackTrace();
		}
		return sendData;
	}
	
	/**
	 * 종목코드를 입력받아 간단보기용 결과를 String 타입으로 리턴
	 * 
	 * @param 	code 
	 * @return	String
	 * @see
	 */
	public static String getStockInfoMINI(String code){
		String sendData = "";
		try{
			Map<String, String> data = ParserUtil.getStockInfo(code);
			if(data.get("isClose").equals("on")) {
				sendData += " " + data.get("nowPrice");
				sendData += " " + data.get("compPrice");
				sendData += " (" + data.get("compPercent") + ") ";
			} else if(data.get("isClose").equals("off")) {
				sendData = data.get("closeInfo");
			}
		}
		catch(Exception e){
			System.out.println("[ParserUtil  -  getStockInfoMINI]  :  Exception");
			e.printStackTrace();
		}
		return sendData;
	}
}
