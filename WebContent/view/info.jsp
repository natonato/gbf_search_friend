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
player id : ${playerInfo.id} <br>
player name : ${playerInfo.name } <br>
player name : ${playerInfo.rank } <br>
	<c:forEach var="sum_element" items="${playerInfo.summon }">
		<c:forEach var="sum" items="${sum_element }">
			summon = ${sum } <br>
			<img alt="" src="${sum }"><br>
			
		</c:forEach>
		<br>
	</c:forEach>
	<c:forEach var="sum_levels" items="${playerInfo.summonLevel }">
		<c:forEach var="sum_level" items="${sum_levels }">
			lvl = ${sum_level }<br>
		</c:forEach>
	</c:forEach>
	<c:forEach var="sum_names" items="${playerInfo.summonName }">
		<c:forEach var="sum_name" items="${sum_names }">
			name = ${sum_name }<br>
		</c:forEach>
	</c:forEach>
</c:if>
</body>
</html>