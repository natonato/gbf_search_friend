<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="root" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	연동 테스트
	<form method="post" action="${root}/main" id="test">
		<input type="hidden" name="act" value="searchProfile">
		<label>사용자 번호</label><input type="text" name="code" id="code"><br>
		
		<button type="submit" value="search">검색하기</button>
		
	</form>
	<form method="post" action="${root}/main" id="test">
		<input type="hidden" name="act" value="twitterTest">
		<button type="submit" value="search">트위터테스트</button>
		
	</form>
	<form method="post" action="${root}/main" id="test">
		<input type="hidden" name="act" value="twitterCookieTest">
		<button type="submit" value="search">트위터쿠키테스트</button>
		
	</form>
	<form method="post" action="${root}/main" id="test">
		<input type="hidden" name="act" value="tweetTest">
		<button type="submit" value="search">트윗테스트</button>
		
	</form>
	<form method="post" action="${root}/main" id="test">
		<input type="hidden" name="act" value="token">
		<label>pin : </label><input type="text" name="token" id="token"><br>
		
		<button type="submit" value="search">트윗 토큰 얻기</button>
		
	</form>
	<form method="post" action="${root}/main" id="test">
		<input type="hidden" name="act" value="tweetUserTest">
		<button type="submit" value="search">트윗계정테스트</button>
		
	</form>
</body>
</html>