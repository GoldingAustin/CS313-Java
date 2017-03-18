<<<<<<< HEAD
<%-- 
    Document   : login
    Created on : Mar 2, 2017, 5:06:21 PM
    Author     : austingolding
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
=======
<%--
  Created by IntelliJ IDEA.
  User: austingolding
  Date: 3/2/17
  Time: 6:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
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
    <div class="wrapper">
        <form id="login" action="Login" method="post">
            <div class="form-group">
                <p>
                    <label for="name">Username:</label>
                    <input id="name" name="name" type="text" class="form-control" required/>
                </p>
                <p>
                    <label for="pass">Password:</label>
                    <input id="pass" name="pass" type="password" class="form-control" required/>
                </p>
            </div>
            <button id="sub" type="submit" value="Login" class="btn btn-default">Submit</button>
        </form>
    </div>
</div>
</body>
<script>
    $("#login").validate({
        rules: {
            pass: "required"
        }
    });
</script>
>>>>>>> origin/master
</html>
