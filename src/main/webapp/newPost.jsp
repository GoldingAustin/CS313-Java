<%@ page import="java.util.Date" %>
<%@ page import="java.sql.Timestamp" %><%--
  Created by IntelliJ IDEA.
  User: austingolding
  Date: 3/2/17
  Time: 6:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    Date timestamp = new Timestamp(System.currentTimeMillis());
    String time = timestamp.toString();
%>
<html>
<head>
    <title>New Post</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <!-- jQuery library-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.15.0/jquery.validate.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.15.0/additional-methods.min.js"></script>
    <!-- Latest compiled JavaScript-->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
<form action="Posts" method="post">
    <div class="form-group">
    <textarea name="post" id="post" class="form-control" rows="5"></textarea>
    </div>
    <input name="timestamp" type="hidden" value="<%=time%>" />
<button type="submit" class="btn btn-default" value="Posts">Submit</button>
</form>
<button type="button" class="btn btn-default" onclick="location.href='Posts'">View Posts</button>
</div>
</body>
</html>
