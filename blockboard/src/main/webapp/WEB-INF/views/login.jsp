<!--
* @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
* @file login.jsp
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/css; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/loginstyle.css">
</head>
<body>
<div class="login-page">
    <div class="form">
        <h1 class="display-1" style="color:#76b852">BLOCK BOARD</h1>
        <form action="/login" method="POST" class="login-form">
            <input type="text" name="userId" placeholder="username"/>
            <input type="password" name="userPassword" placeholder="password"/>
            <button type="submit">login</button>
        </form>
    </div>
</div>
</body>

</html>