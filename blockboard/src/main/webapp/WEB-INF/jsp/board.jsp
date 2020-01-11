<!-- board.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>mysql 연동</title>
</head>

<body>
    <h3>Login Success!!!</h3>
    <h3>Login ID : <%=(String)session.getAttribute("USER") %></h3>
    <a href="logout">로그아웃</a>

    <h2>게시판 목록</h2>
	<table border="1">
		<tr>
			<th>게시판 목록</th>
		</tr>
        <c:forEach items="${list}" var = "list">
                    			<tr>
                    				<td><c:out value="${list.board_name}" /></td>
                    			</tr>
                    		</c:forEach>
	</table>

	<table width="100%" cellpadding="0" cellspacing="0" border="0">
      <tr height="5"><td width="5"></td></tr>
     <tr style="background:url('img/table_mid.gif') repeat-x; text-align:center;">
       <td width="5"><img src="img/table_left.gif" width="5" height="30" /></td>
       <td width="73">번호</td>
       <td width="379">제목</td>
       <td width="73">작성자</td>
       <td width="164">작성일</td>
       <td width="7"><img src="img/table_right.gif" width="5" height="30" /></td>
      </tr>
    <tr height="25" align="center">
    </tr>
      <tr height="1" bgcolor="#D2D2D2"><td colspan="6"></td></tr>

     <tr height="1" bgcolor="#82B5DF"><td colspan="6" width="752"></td></tr>
     </table>

</body>

</html>