<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.common.util.DbUtil"%>
<%@ page import="com.common.util.ParserUtil"%>
<%@ page import="java.sql.*"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Idea Bot</title>
<style type="text/css">
* {margin:0px; auto;}
#wrap{width:100%;}
#head{height:80px;background:#eee;}
#mention,#body,#body1,#body2,#body3,#login{float:left;}
#mention,#body,#login{height:700px;}
#mention{width:40%;background:#FFD2D3;}
#body{width:30%;}
#body1{width:100%;height:250px;background:#aaaaaa;}
#body2{width:100%;height:200px;background:#bbbbbb;}
#body3{width:100%;height:250px;background:#eeeeee;}
#login{width:30%;background:#CCCCCC;}
#foot{clear:both;height:30px;background:#eee;}
</style>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
<script type="text/javascript" src="/dwr/interface/indexDwrService.js"></script>
<script type="text/javascript" src="/dwr/engine.js"></script>
<script type="text/javascript" src="/dwr/util.js"></script>
<script type="text/javascript">
<!--
window.onload = func_today;

function func_today() {
    var now = new Date();
    var hours = now.getHours();
    var minutes = now.getMinutes();
    var seconds = now.getSeconds();
    var strtime = (hours >= 12 ? "PM " : "AM ");
    strtime += (hours > 12 ? hours - 12 : hours);
    strtime += (minutes < 10 ? ":0" : ":") + minutes;
    strtime += (seconds < 10 ? ":0" : ":") + seconds;
    document.getElementById("clock").innerHTML = strtime;
   
    setTimeout("func_today()", 1000);
}
function twitt_mention(){
	document.getElementById("content").innerHTML = "";
	setTimeout("twitt_mention()", 1000);
}

function searchStockStatus() {
	var stockCd = $("#stockCd").val();
	
	indexDwrService.getDaumFinancialValue(stockCd, {
		callback: searchStockStatusCallback,
		errorHandler:function(msg, exception) {
			alert("에러메세지  :  " + msg + "\n상세내용  :  " + dwr.util.toDescriptiveString(exception, 2));
		}
	});
}

function searchStockStatusCallback(data) {
	dwr.util.setValue("stockCdRslt", data);
}
//-->
</script>
</head>
<body>
 
<div id="wrap">
	<div id="head">
		<h1>1. 타이틀 IdeaBot Coming Soon...</h1>
		<div id="clock"></div>
	</div> 
	<div id="mention">
		2. 멘션<br>
		<c:forEach var="i" items="${_TWITTER }">
		${i.getText() }<br>
		</c:forEach>
	</div>
	<div id="body">
		<div id="body1">
			3. 증시관련<br>
			<%
			DbUtil du = new DbUtil();
			
			ResultSet rs = du.queryResult("select KR_CODE, STOCK_NAME_KOR from stock_info limit 0,10");
			while(rs.next()){
				out.println("<a href='#' onclick=\"window.open('http://stock.daum.net/item/main.daum?code="+rs.getString("KR_CODE")+"');return false;\">"+rs.getString("STOCK_NAME_KOR")+"("+rs.getString("KR_CODE")+")</a><br>");
			
			}
			%>
		</div> 
		<div id="body2">
			4. 추가서비스1<br>
			<%
			Map<String, String> data = ParserUtil.getStockInfo("047810");
			
			String sendData = "";
			if(data.get("isClose").equals("on")) {
				sendData = data.get("nameCode") + "\n현재가: " + data.get("nowPrice") + "\n전일대비: (" + data.get("compPercent") + ") ";
				if(data.get("compPercent").contains("+")) sendData = sendData + "▲" + data.get("compPrice");
				else if(data.get("compPercent").contains("-")) sendData = sendData + "▽" + data.get("compPrice");
				else sendData = sendData + data.get("compPrice");
				
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
				sendData += "(출처: Daum 증권)";
			} else if(data.get("isClose").equals("off")) {
				sendData = data.get("closeInfo");
			}
			out.println(sendData);
			%>
		</div> 
		<div id="body3">
			5. 추가서비스2<br>
			<label>검색 (주식)</label>
			<input type="text" size="15" name="stockCd" id="stockCd">
			<input type="button" value="검색" onclick="searchStockStatus();">
			<br>
			<div id="stockCdRslt">
			</div>
		</div>
	</div>
	<div id="login">
		6. 로그인정보
	</div> 
	<div id="foot">
		footer
	</div>
</div>

</body>
</html>