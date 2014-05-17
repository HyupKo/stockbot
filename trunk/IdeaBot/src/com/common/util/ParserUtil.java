package com.common.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


public class ParserUtil {
	public ParserUtil(){}
	public static Map<String, String> getStockInfo(String code) throws IOException{
		String addr = new String("http://stock.daum.net/item/main.daum?code="+code);
		URL url = new URL(addr);
		URLConnection connection = url.openConnection();
		InputStream is = (InputStream) connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		Map<String,String> map = new HashMap<String,String>();
		String buf = null;
		int sc2=0;
		while((buf = br.readLine())!=null){
			if(buf.matches(".*topNotice.*")){
				int sc1=0;
				while(true){
					buf = br.readLine().trim();
					if(buf.matches(".*sprites.*")){
						break;
					}
					buf = buf.replaceAll("<[^>]*>","");
					if(!buf.equals("")){
						buf = buf.trim();
						if(sc1==0){
							map.put("closeInfo",buf);
							map.put("isClose","off");
						}
						sc1++;
					}
				}
			}
			if(buf.matches(".*CT_ZONE_DETAILTOPINFO.*")){
				while(true){
					buf = br.readLine().trim();
					if(buf.matches(".*askedPrice.*")){
						break;
					}
					buf = buf.replaceAll("<[^>]*>","");
					if(!buf.equals("")){
						buf = buf.trim();
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
				while(true){
					buf = br.readLine().trim();
					if(buf.matches(".*upjongNTheme.*")){
						break;
					}
					buf = buf.replaceAll("<[^>]*>","");
					if(!buf.equals("")){
						buf = buf.trim();
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
	public static String getStockInfoDM(String code){
		String sendData = "";
		try{
			Map<String, String> data = ParserUtil.getStockInfo(code);
			if(data.get("isClose").equals("on")) {
				sendData += data.get("nameCode") + "\n  " + data.get("nowPrice") + " ";
				sendData += data.get("compPrice");
				sendData += " (" + data.get("compPercent") + ") ";
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
}
