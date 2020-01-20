<%@ page language="java" contentType="text/html; charset=UTF-8"
           pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
   <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
   <title>로그인 화면</title>

   <!-- css 파일 나중에 넣기 -->

</head>

<body>
   </div>
   <div id="wrap-login">
   <form action="loginCheck" method="POST" style="width: 470px;">
        <h2>로그인</h2>
        <input type="text" name="user_id" id="user_id" class="w3-input" placeholder="아이디"><br>
        <input type="password" name="user_pwd" id="user_pwd" class="w3-input" placeholder="비밀번호"><br>
        <input type="submit" value="로그인"><br>
    </form>
    </div>
</body>

</html>