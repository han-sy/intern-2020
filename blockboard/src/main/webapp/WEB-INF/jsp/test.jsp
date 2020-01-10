<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
	<title>mysql 연동</title>
</head>

<body>
	<table border="1">
		<tr>
			<th>회원번호</th>
			<th>회사번호</th>
			<th>회원타입</th>
			<th>jsp에서 보낸 id</th>
			<th>jsp에서 보낸 pwd</th>
		</tr>
		<tr>
			<td>${user_id}</td>
			<td>${com_id}</td>
			<td>${user_type}</td>
			<td>${id}</td>>
			<td>${pwd}</td>>
		</tr>
	</table>
</body>

</html>