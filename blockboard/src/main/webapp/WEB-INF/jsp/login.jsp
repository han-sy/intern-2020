<%@ page language="java" contentType="text/html; charset=UTF-8"
           pageEncoding="UTF-8"%>
       <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
       <html>
       <head>
       	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
		   <title>로그인 화면</title>
		   
		   <!-- css 파일 나중에 넣기 -->

       </head>

       <body>
           <form action="board" method="POST" style="width: 470px;">
				<h2>로그인</h2>
				<input type="text" name="id" id="id" class="w3-input" placeholder="아이디"><br>
				<input type="password" name="pwd" id="pwd" class="w3-input" placeholder="비밀번호"><br>
				<input type="submit" value="로그인"><br>
			</form>
       </body>

       </html>