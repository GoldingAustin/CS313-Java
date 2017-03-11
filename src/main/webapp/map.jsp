<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: austingolding
  Date: 3/7/17
  Time: 5:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Post Map</title>
    <meta name="viewport" content="initial-scale=1.0">
    <meta charset="utf-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <!-- jQuery library-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.15.0/jquery.validate.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.15.0/additional-methods.min.js"></script>
    <!-- Latest compiled JavaScript-->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <style>
        #map {
            height: calc(100% - 55px);
        }
        html, body {
            height: 100%;

            padding: 0;
        }
        .navbar-form .form-control {
            display: inline-block;
            width: auto;
            vertical-align: middle;
        }

        .navbar {
            margin-bottom: 0;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <form class="navbar-form">
            <div class="form-group" style="display:inline; width: 100%;">
                <div class="input-group" style="width: 100%;">
                    <input type="text" class="form-control" placeholder="Search by hashtag or username">
                    <span class="input-group-addon"><span class="glyphicon glyphicon-search"></span></span>
                </div>
            </div>
        </form>
    </div>
</nav>
<div id="map"></div>
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
<script>
    var map;
    function initMap() {
        map = new google.maps.Map(document.getElementById('map'), {
            center: {lat: -34.397, lng: 150.644},
            zoom: 8
        });
    }
</script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAdsjlZu63hLWiK0D02VRC1cj2JdlDsgNU&callback=initMap"
        async defer></script>
</body>
</html>
