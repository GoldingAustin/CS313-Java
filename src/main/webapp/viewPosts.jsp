<%--
  Created by IntelliJ IDEA.
  User: austingolding
  Date: 3/2/17
  Time: 7:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>View Posts</title>
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
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
        <tr>
        <th>Name</th>
        <th>Post</th>
        <th>Post Time</th>
        </tr>
        </thead>
<tbody>
<c:forEach var="post" items="${posts}">
   <tr> <td>${post.getName()}</td><td> ${post.getPost()} </td> <td>${post.getTimestamp()}</td></tr>
</c:forEach>
</tbody>
    </table>
</div>
<button id="sub" type="button" class="btn btn-default" onclick="location.href='newPost.jsp'">New Post</button>
</div>
</body>
</html>
