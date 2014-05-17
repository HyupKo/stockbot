<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
<!--
#wrap {width: 800px; margin: 0px auto; }

.userNm {color: blue; font-size: 14px; font-weight: bold;}
-->
</style>
<title>iBot  ::  상품번호 등록</title>
</head>
<body>
<div id="wrap">
	<div id="container">
		<form method="post" action="/form/regist/">
			<span class="userNm">${USER_NM }</span> 님 상품을 등록해 주세요.
			<label>상품번호  :  </label>
			<input type="text" id="krCord" name="krCord" value=""><br>
			<span class="impt">매시간은 9시 부터 3시까지 입니다.</span>
			<select id="activateTime" name="activateTime">
				<option value="1">매 시간.</option>
			</select>
			<input type="submit">
		</form>
	</div>
</div>
</body>
</html>