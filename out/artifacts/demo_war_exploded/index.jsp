<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>iwap5.0</title>
</head>
<body>
<%
if(session.getAttribute("userInfo")==null){
	response.sendRedirect("./iwap.ctrl?txcode=login");
}else{
	request.getRequestDispatcher("/screen/mainFrame.jsp").forward(request, response);
}

%>
</body>
</html>