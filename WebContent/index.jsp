<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="root" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	���� �׽�Ʈ
	<form method="post" action="${root}/main" id="test">
		<input type="hidden" name="act" value="searchProfile">
		<!-- <label>����� ��ȣ</label><input type="text" name="code" id="code"><br>
		 -->
		<button type="submit" value="search">�˻��ϱ�</button>
		
	</form>
	<form method="post" action="${root}/main" id="test">
		<input type="hidden" name="act" value="twitterTest">
		<button type="submit" value="search">Ʈ�����׽�Ʈ</button>
		
	</form>
	<form method="post" action="${root}/main" id="test">
		<input type="hidden" name="act" value="twitterCookieTest">
		<button type="submit" value="search">Ʈ������Ű�׽�Ʈ</button>
		
	</form>
</body>
</html>