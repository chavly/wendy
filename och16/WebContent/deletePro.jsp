<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<c:if test="${result > 0}">
	<script type="text/javascript">
		alert("삭제완료!");
		location.href="list.do?pageNum=${pageNum}";
	</script>
</c:if>
<c:if test="text/javascript">
	<script type="text/javascript">
		alert("헐 암호틀려!");
		location.href="deleteForm.do?num=${num}&pageNum=${pageNum}";
	</script>
</c:if>
</head>
<body>

</body>
</html>