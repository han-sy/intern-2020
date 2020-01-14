<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.util.Enumeration" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
	<title>글쓰기</title>
</head>

<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="../SE2/js/service/HuskyEZCreator.js"></script>
<body>
    <h2>글쓰기 페이지입니다.</h2>
    <form action="/board/post/write" method="post">
    <textarea name="smarteditor" id="smarteditor" rows="10" cols="100"></textarea></br>
    <input type="submit" value="글쓰기"/>
    </form>


</body>

</html>