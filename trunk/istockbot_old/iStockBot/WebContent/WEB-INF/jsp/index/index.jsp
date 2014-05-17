<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Idea Bot</title>

<link rel="STYLESHEET" type="text/css" href="/common/css/style.css">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
<script type="text/javascript" src="/dwr/interface/indexDwrService.js"></script>
<script type="text/javascript" src="/dwr/engine.js"></script>
<script type="text/javascript" src="/dwr/util.js"></script>
<script type="text/javascript">
<!--
/* Global Var */
var paging=1; 
var stockKrCod;
var refreshTime = 3000;
var autoRefresh = true;
/**************/

window.onload = function(){
	func_today();
	stockKrCode="<c:out value="${_DEFAULT_KRCODE }"/>";
	if(timeCheck()){
		func_refresh();
	}
}
function timeCheck(){
	var now = new Date();
	var hours = now.getHours();
	var day = now.getDay();
	
	if((day>=1&&day<=5&&hours>=9&&hours<=15)&&autoRefresh)	{
		$("#timeOnOff").html("<a href='#' onclick='autoRefresh=false;func_refresh();'>새로고침On(갱신주기:"+refreshTime/1000+"초)</a>");
		return true;
	}
	else {
		$("#timeOnOff").html("<a href='#' onclick='autoRefresh=true;func_refresh();'>새로고침Off</a>");
		return false;
	}
}
//시계 함수
function func_today() {
    var now = new Date();
    var hours = now.getHours();
    var minutes = now.getMinutes();
    var seconds = now.getSeconds();
    var strtime = (hours >= 12 ? "PM " : "AM ");
    strtime += (hours > 12 ? hours - 12 : hours);
    strtime += (minutes < 10 ? ":0" : ":") + minutes;
    strtime += (seconds < 10 ? ":0" : ":") + seconds;
    $("#clock").html(strtime);
    setTimeout("func_today()", 1000);
}
function func_refresh(){
	refreshIndexInfo();
	refreshStockInfo();
	if(timeCheck()){
		setTimeout("func_refresh()", refreshTime);
	}
}
//종목검색 함수
function searchStockStatus(chk) {
	var searchStr = $("#stockCd").val();
	var stockKind = $("#selKind").val();
	var jsonData = {
			"searchStr": searchStr,
			"stockKind": stockKind
		};
	if(searchStr.length<2&&chk=="1"){
		alert("2글자 이상 입력해주세요.");
		return;
	}
	if(searchStr){
		indexDwrService.getDaumFinancialValue(jsonData, {
			callback: searchStockStatusCallback,
			errorHandler:function(msg, exception) {
				alert("에러메세지  :  " + msg + "\n상세내용  :  " + dwr.util.toDescriptiveString(exception, 2));
			}
		});
	}
}
//종목검색 콜백
function searchStockStatusCallback(data) {
	document.getElementById("stockCdRslt").style.display="block";
	document.getElementById("mention").style.display="none";
	var innerSTR="<a href='#' onclick='nextPageMention(paging);'><--TimeLine으로..</a><br><br>";
	for(n in data){
		var inKrCode = data[n].krCode;
		var inStockName = data[n].stockName;
		var inHaveCheck = data[n].twitterId;
		var inButton = "";
		if(inHaveCheck!=null){
			inButton = "&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' value='삭제' onclick=\"removeCustTwitterInfo('"+inKrCode+"');\" >";
		}
		else {
			inButton = "&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' value='추가' onclick=\"addOverlaySet('"+inKrCode+"','"+inStockName+"');\">";
		} 
		innerSTR+="<a href='#' onclick=\"stockKrCode='"+inKrCode+"';refreshStockInfo();return false;\">"+inStockName+" ("+inKrCode+")</a>"+inButton+"<br>";
	}
	if(!innerSTR)innerSTR+="검색결과가 없습니다.";
	dwr.util.setValue("stockCdRslt", innerSTR,{ escapeHtml:false });
}
//타임라인 로드
function nextPageMention(page){
	document.getElementById("stockCdRslt").style.display="none";
	document.getElementById("mention").style.display="block";
	indexDwrService.getTwitterTimeLine(page, {
		callback: nextPageMentionCallback,
		callbackArg: page,
		errorHandler:function(msg, exception) {
			alert("에러메세지  :  " + msg + "\n상세내용  :  " + dwr.util.toDescriptiveString(exception, 2));
		}
	});
}
//타임라인 콜백
function nextPageMentionCallback(data, arg1){
	if(arg1=="1"){
		dwr.util.setValue("ment", data,{ escapeHtml:false });
		paging=2;
	}
	else {
		document.getElementById("ment").innerHTML += data;
	}
	
}
//트윗버튼이벤트
function mentionInput(){
	var mentCont = $("#mentCont").val();
	if(!mentCont){
		alert("내용을 입력해주세요.");
		document.getElementById("mentCont").focus();
		return;
	}
	indexDwrService.setTwitterMention(mentCont, {
		callback: mentionInputCallback,
		errorHandler:function(msg, exception) {
			alert("에러메세지  :  " + msg + "\n상세내용  :  " + dwr.util.toDescriptiveString(exception, 2));
		}
	});
}
//트윗버튼 콜백
function mentionInputCallback(data){
	if(data=="S"){
		$("#mentCont").val("");
		setTimeout("nextPageMention(1)", 1500);
	}
	else if(data=="F"){
		alert("멘션 입력 실패");
	}
}
//인덱스정보갱신
function  refreshIndexInfo(){
	indexDwrService.getParseIndexInfo( {
		callback: refreshIndexInfoCallback,
		errorHandler:function(msg, exception) {
			alert("에러메세지  :  2" + msg + "\n상세내용  :  " + dwr.util.toDescriptiveString(exception, 2));
		}
	});
}
function refreshIndexInfoCallback(data){
	dwr.util.setValue("indexinfo", data,{ escapeHtml:false });
}
//주식 정보 갱신
function refreshStockInfo(){
	indexDwrService.getParseStockInfo(stockKrCode, {
		callback: refreshStockInfoCallback,
		errorHandler:function(msg, exception) {
			alert("에러메세지  :  1" + msg + "\n상세내용  :  " + dwr.util.toDescriptiveString(exception, 2));
		}
	});
}
function refreshStockInfoCallback(data){
	dwr.util.setValue("stockinfo", data,{ escapeHtml:false });
}
//등록된 종목 삭제
function removeCustTwitterInfo(kr_code) {
	if(!confirm('삭제하시겠습니까?')) return;
	var jsonData = {
		"krCode": kr_code,
		"twitterId": ""
	};
	
	indexDwrService.removeCustTwitterInfo(jsonData, {
		callback: removeCustTwitterInfoCallback,
		errorHandler:function(msg, exception) {
			alert("에러메세지  :  " + msg + "\n상세내용  :  " + dwr.util.toDescriptiveString(exception, 2));
		}
	});
}
function removeCustTwitterInfoCallback(data) {
	if(data == "S") {
		alert("삭제되었습니다.");
		loadUserStockInfo();
		searchStockStatus();
	} else {
		alert("서버에 문제가 발생하였습니다.\n다음에 다시 시도해 주시기 바랍니다.");
	}
}
function addOverlaySet(krCode,stName){
	$("#saveKrCode").html(stName+"("+krCode+")");
	$("#saveQuantity").val("");
	$("#savePrice").val("");
	$("#saveActiveTime").val("");
	$("#modifyBtn").html("<a href='#' onclick=\"addStockInfo('"+krCode+"');\">[등록]</a>");
	overlay();
}
//종목 등록
function addStockInfo(krCode,stName){
	if(!confirm("등록하시겠습니까?")) return false;
	var sQuantity = $("#saveQuantity").val();
	var sPrice = $("#savePrice").val();
	var sActiveTime = $("#saveActiveTime").val();
	var jsonData = {
		"krCode": krCode,
		"twitterId": "",
		"stQuantity": sQuantity,
		"stPrice": sPrice,
		"activateTime": sActiveTime
	};
	indexDwrService.createStockInfo(jsonData, {
		callback: addStockInfoCallback,
		errorHandler:function(msg, exception) {
			alert("에러메세지  :  " + msg + "\n상세내용  :  " + dwr.util.toDescriptiveString(exception, 2));
		}
	});
}
function addStockInfoCallback(data){
	if(data == "S") {
		alert("등록되었습니다.");
		overlay();
		loadUserStockInfo();
		searchStockStatus();
	}
	else {
		alert("서버에 문제가 발생하였습니다.\n다음에 다시 시도해 주시기 바랍니다.");
	}
}
//저장된 사용자 종목정보 조회
function loadUserStockInfo(){
	var jsonData = {
		"twitterId": ""
	};
	indexDwrService.getInfoList(jsonData, {
		callback: loadUserStockInfoCallback,
		errorHandler:function(msg, exception) {
			alert("에러메세지  :  " + msg + "\n상세내용  :  " + dwr.util.toDescriptiveString(exception, 2));
		}
	});
}
function loadUserStockInfoCallback(data){
	var innerSTR="";
	for(n in data){
		var inKrCode = data[n].krCode;
		var inStockName = data[n].stockName;
		var inActiveTime = data[n].activateTime;
		var inButton = "";
		if(inActiveTime=='1'){
			inButton = "매시간";
		}
		else if(inActiveTime=='2'){
			inButton = "30분";
		}
		else {
			inButton = "미사용";
		} 
		innerSTR+="<a href='#' onclick=\"stockKrCode='"+inKrCode+"';refreshStockInfo();return false;\">"+inStockName+"("+inKrCode+")</a>"+inButton+"<input type=\"button\" value=\"수정\" onclick=\"loadUserInfo('"+inKrCode+"');\"/><input type='button' value='삭제' onclick=\"removeCustTwitterInfo('"+inKrCode+"');\"><br>";
	}
	if(!innerSTR)innerSTR+="검색결과가 없습니다.";
	dwr.util.setValue("custList", innerSTR,{ escapeHtml:false });
}
//등록종목 정보
function loadUserInfo(krCode){
	var jsonData = {
			"twitterId": "",
			"krCode": krCode
		};
		indexDwrService.getUserInfoList(jsonData, {
			callback: loadUserInfoCallback,
			callbackArg: krCode,
			errorHandler:function(msg, exception) {
				alert("에러메세지  :  " + msg + "\n상세내용  :  " + dwr.util.toDescriptiveString(exception, 2));
			}
		});
}
function loadUserInfoCallback(data, arg1){
	for(n in data){
		var inStName = data[n].stockName;
		var inStQuant = data[n].stQuantity;
		var inStPrice = data[n].stPrice;
		var inActivateTime = data[n].activateTime;
		dwr.util.setValue("saveKrCode", inStName+"("+arg1+")");
		dwr.util.setValue("saveQuantity", inStQuant);
		dwr.util.setValue("savePrice", inStPrice);
		dwr.util.setValue("saveActiveTime",inActivateTime);
		dwr.util.setValue("modifyBtn","<a href='#' onclick=\"modifyUserInfo('"+arg1+"');\">[수정]</a>",{ escapeHtml:false });
	}
	overlay();
}
//등록종목 정보 수정
function modifyUserInfo(krCode){
	var sQuantity = $("#saveQuantity").val();
	var sPrice = $("#savePrice").val();
	var sActiveTime = $("#saveActiveTime").val();
	var jsonData = {
		"twitterId": "",
		"krCode": krCode,
		"stQuantity": sQuantity,
		"stPrice": sPrice,
		"activateTime": sActiveTime
	};
	indexDwrService.UpdateUserInfoList(jsonData, {
		callback: modifyUserInfoCallback,
		errorHandler:function(msg, exception) {
			alert("에러메세지  :  " + msg + "\n상세내용  :  " + dwr.util.toDescriptiveString(exception, 2));
		}
	});
}
function modifyUserInfoCallback(data){
	if(data == "S") {
		alert("수정되었습니다.");
		refreshStockInfo();
		loadUserStockInfo();
		overlay();
	}
	else {
		alert("서버에 문제가 발생하였습니다.\n다음에 다시 시도해 주시기 바랍니다.");
	}
}
//모달레이어 호출
function overlay() {
	el = document.getElementById("overlay");
	if(el.style.display == "block"){
		$('#overlay').fadeOut('fast');
	}
	else {
		$('#overlay').fadeIn('fast');
	}
}
//-->
</script>
</head>
<body>
 
<div id="wrap">
	<div id="head">
		<div id="userInfo" style="float:right;padding-right:8px"><c:out value="${_TWITTINFO }" escapeXml="false"/></div>
		<div id="headCont"><h3>Coming Soon...</h3></div>
		<div id="clock"></div>
		<div id="headSearch">
			<select	name="selKind" id="selKind">
				<option value="0">전체</option>
				<option value="1">KOSPI</option>
				<option value="2">KOSDAQ</option>
			</select>
			<input type="text" size="15" name="stockCd" id="stockCd" onkeyup="if(event.keyCode==13)searchStockStatus(1);">
			<input type="button" value="검색" onclick="searchStockStatus(1);">
			<div id="timeOnOff" style="float:right;padding-right:8px"></div>
		</div>
	</div> 
	<div id="stockCdRslt" style="display:none;"></div>
	<div id="mention">
		<textarea id="mentCont" name="mentCont" rows="3" cols="50"></textarea>
		<br>
		<input type="button" value="tweet" onclick="mentionInput();">
		<br>
		<div id="ment">
		<c:out value="${_TWITTER }" escapeXml="false"/>
		</div>
		<br>
		<input type="button" value="10개 더" onclick="paging++;nextPageMention(paging);">
	</div>
	<div id="body">
		<div id="body1">
			주요지수<br>
			<div id="indexinfo">
				<c:out value="${_INDEXINFO }" escapeXml="false"/>
			</div>
		</div> 
		<div id="body2">
			관심종목<br>
			<div id="stockinfo">
				<c:out value="${_STOCKINFO }" escapeXml="false"/>
			</div>
		</div> 
		<div id="body3">
			등록종목
			<div id="custList">
				<c:forEach var="i" items="${_INFO_LIST }">
					<a href='#' onclick="stockKrCode='${i.krCode }';refreshStockInfo();return false;">
					${i.stockName } (${i.krCode })
					</a>
						<c:choose>
							<c:when test="${i.activateTime eq '1' }">
								매시간
							</c:when>
							<c:when test="${i.activateTime eq '2' }">
								30분
							</c:when>
							<c:otherwise>
								미사용
							</c:otherwise>
						</c:choose>
					<input type="button" value="수정" onclick="loadUserInfo('${i.krCode }');"/>
					<input type="button" value="삭제" onclick="removeCustTwitterInfo('${i.krCode }');"><br>
				</c:forEach>
			</div>
		</div>
	</div>
</div>
<div id="overlay">
     <div>
     	등록종목 : <span id="saveKrCode"></span><br>
     	보유수량 : <input type="text" size=5 id="saveQuantity"><br>
     	평균매입가 : <input type="text" size=12 id="savePrice"><br>
     	수신시간 : <select id="saveActiveTime">
     				<option value="0">미사용</option>
     				<option value="1">매시간</option>
     				<option value="2">30분</option>
     			</select><br>
     	<span id="modifyBtn"></span>&nbsp;&nbsp;[<a href='#' onclick='overlay()'>닫기</a>]
     </div>
</div>
</body>
</html>