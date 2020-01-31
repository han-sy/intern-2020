<!--
* @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
* @file login.jsp
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <link rel="stylesheet" type="text/css" href="/static/css/loginstyle.css">
</head>
<body>
<div class="login-page">
    <div class="form">
        <form action="/login" method="POST" class="login-form">
            <input type="text" name="userID" placeholder="username"/>
            <input type="password" name="userPassword" placeholder="password"/>
            <button type="submit">login</button>
        </form>
    </div>
</div>
</body>

</html>