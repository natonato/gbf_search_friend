<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<c:if test="${empty playerInfo}">
There is no user
</c:if>
<c:if test="${!empty playerInfo}">
player name : ${playerInfo.name } <br>
player name : ${playerInfo.rank } <br>
	<c:forEach var="sum_element" items="${playerInfo.summon }">
		<c:forEach var="sum" items="${sum_element }">
			summon = ${sum } <br>
			<img alt="" src="${sum }"><br>
		</c:forEach>
		<br>
	</c:forEach>
</c:if>
</body>
</html>