<%@ page import="org.jinstagram.auth.oauth.InstagramService" %>
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
<link>
<title>Post Map</title>
<meta name="viewport" content="initial-scale=1.0">
<meta charset="utf-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<!-- jQuery library-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/OverlappingMarkerSpiderfier/0.3.3/oms.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.15.0/jquery.validate.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.15.0/additional-methods.min.js"></script>
<script src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script src='sortable.min.js'></script>
<link rel='stylesheet' href='sortable-theme-bootstrap.css'>
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
        <form class="navbar-form" id="submitForm" action="Map" method="POST">
            <div class="form-group" style="display:inline; width: 100%;">
                <div class="input-group" style="width: 100%;">
                    <input type="text" name="search" class="form-control" placeholder="Search by hashtag or username">
                    <input style="display: none;" type="text" id="centerLat" name="centerLat">
                    <input style="display: none;" type="text" id="centerLong" name="centerLong">
                    <input style="display: none;" type="text" id="radius" name="radius">
                    <span class="input-group-addon btn" onclick="document.getElementById('submitForm').submit();"><span
                            class="glyphicon glyphicon-search"></span></span>
                </div>
            </div>
        </form>
    </div>
</nav>
<div id="map"></div>
<div class="table-responsive">
    <table class="table table-striped sortable-theme-bootstrap results" data-sortable="">
        <thead>
        <tr>
            <th data-sortable-type="alpha">Source</th>
            <th data-sortable-type="alpha">Name</th>
            <th data-sortable-type="alpha">Post</th>
            <th data-sortable-type="date">Post Time</th>
            <th data-sortable-type="numeric" style="display: none;">Lat</th>
            <th data-sortable-type="numeric" style="display: none;">Lng</th>
        </tr>
        </thead>
        <tbody id="tableBody">
        <c:forEach var="post" items="${posts}">
            <tr>
                <td class="source"><a href='<c:out value="${post.getUrl()}"/>'>${post.getType()}</a></td>
                <td class="poster">${post.getName()}</td>
                <td class="post"> ${post.getPost()} </td>
                <td class="time">${post.getTimestamp()}</td>
                <td class="lat" style="display: none;"> ${post.getLocation().getLatitude()} </td>
                <td class="long" style="display: none;"> ${post.getLocation().getLongitude()} </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script>


    var gm = google.maps;
    var map;
    var timer = 0;
    var setBox = false;
    function initMap() {
        // Load OMS dynamically now that Google Maps API has loaded
        var script_tag = document.createElement('script');
        script_tag.setAttribute("type", "text/javascript");
        script_tag.setAttribute("src", "oms.min.js");
        (document.getElementsByTagName("head")[0] || document.documentElement).appendChild(script_tag);
        // initiate loading of oms and initialise map
        setTimeout(loadMap, 50);
    }

    function CenterControl(controlDiv) {

        // Set CSS for the control border.
        var controlUI = document.createElement('div');
        controlUI.style.backgroundColor = '#fff';
        controlUI.style.border = '2px solid #fff';
        controlUI.style.borderRadius = '3px';
        controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
        controlUI.style.cursor = 'pointer';
        controlUI.style.marginBottom = '22px';
        controlUI.style.textAlign = 'center';
        controlUI.title = 'Click to create bounding box to find posts in that area';
        controlDiv.appendChild(controlUI);

        // Set CSS for the control interior.
        var controlText = document.createElement('div');
        controlText.style.color = 'rgb(25,25,25)';
        controlText.style.fontFamily = 'Roboto,Arial,sans-serif';
        controlText.style.fontSize = '16px';
        controlText.style.lineHeight = '38px';
        controlText.style.paddingLeft = '5px';
        controlText.style.paddingRight = '5px';
        controlText.innerHTML = 'Create Box To Find Posts in Area';
        controlText.setAttribute("id", "setBoxText");
        controlUI.appendChild(controlText);

        // Setup the click event listeners: simply set the map to Chicago.
        controlUI.addEventListener('click', function () {
            if (setBox === true) {
                setBox = false;
                document.getElementById("setBoxText").style.color = 'black';
            } else {
                setBox = true;
                document.getElementById("setBoxText").style.color = 'green';
            }
        });

    }


    function loadMap() {
        if (typeof OverlappingMarkerSpiderfier == 'function') {
            map = new google.maps.Map(document.getElementById('map'), {
                center: {lat: 0, lng: 0},
                zoom: 3
            });
            var mousePress = false;
            var box = null;
            var boxFirst = null;
            var boxSecond = null;
            google.maps.event.addListener(map, 'mousemove', function (data) {
                if (mousePress && setBox) {
                    if (box !== null) {
                        var bounds = new google.maps.LatLngBounds(boxFirst, boxSecond);
                        bounds.extend(data.latLng);
                        box.setBounds(bounds);
                    } else {
                        box = new google.maps.Rectangle({
                            map: map,
                            bounds: null,
                            fillOpacity: 0.15,
                            strokeWeight: 0.9,
                            clickable: false
                        });
                    }
                }
            });

            google.maps.event.addListener(map, 'mousedown', function (data) {
                if (setBox) {
                    mousePress = true;
                    boxFirst = data.latLng;
                    map.setOptions({
                        draggable: false
                    });
                }
            });

            google.maps.event.addListener(map, 'mouseup', function (data) {
                if (mousePress && setBox) {
                    mousePress = false;
                    map.setOptions({
                        draggable: true
                    });
                    if (box !== null) // box exists
                    {
                        /**
                         * http://stackoverflow.com/questions/3525670/radius-of-viewable-region-in-google-maps-v3
                         */
                        var bounds = box.getBounds();
                        var center = bounds.getCenter();
                        if (bounds && center) {
                            var ne = bounds.getNorthEast();
                            // Calculate radius (in meters).
                            var radius = google.maps.geometry.spherical.computeDistanceBetween(center, ne);
                            document.getElementById("centerLat").value = center.lat();
                            document.getElementById("centerLong").value = center.lng();
                            document.getElementById("radius").value = radius;
                        }
                    }
                }
            });
            var centerControlDiv = document.createElement('div');
            var centerControl = new CenterControl(centerControlDiv);

            centerControlDiv.index = 1;
            map.controls[google.maps.ControlPosition.TOP_RIGHT].push(centerControlDiv);
            addMarkers();
        } else {
            if (timer < 1000) {
                timer += 50;
                setTimeout(loadMap, 50);
            } else {
                alert('OverlappingMarkerSpiderfier taking too long to load');
                throw('giving up!')
            }
        }
    }

    function addMarkers() {
        var markers = [];
        var iw = new gm.InfoWindow();
        var oms = new OverlappingMarkerSpiderfier(map,
            {markersWontMove: true, markersWontHide: true});
        var usualColor = 'eebb22';
        var spiderfiedColor = 'ffee22';
        var iconWithColor = function (color) {
            return 'https://chart.googleapis.com/chart?chst=d_map_xpin_letter&chld=pin|+|' +
                color + '|000000|ffff00';
        };
        var shadow = new gm.MarkerImage(
            'https://www.google.com/intl/en_ALL/mapfiles/shadow50.png',
            new gm.Size(37, 34),  // size   - for sprite clipping
            new gm.Point(0, 0),   // origin - ditto
            new gm.Point(10, 34)  // anchor - where to meet map location
        );

        oms.addListener('click', function (marker) {
            iw.setContent(marker.desc);
            iw.open(map, marker);
        });

        oms.addListener('spiderfy', function (markers) {
            for (var i = 0; i < markers.length; i++) {
                markers[i].setIcon(iconWithColor(spiderfiedColor));
                markers[i].setShadow(null);
            }
            iw.close();
        });
        oms.addListener('unspiderfy', function (markers) {
            for (var i = 0; i < markers.length; i++) {
                markers[i].setIcon(iconWithColor(usualColor));
                markers[i].setShadow(shadow);
            }
        });

        var tbod = document.getElementById('tableBody');
        var row = tbod.getElementsByTagName("tr");
        var lat = 0.0;
        var lng = 0.0;
        for (var i = 0; i < row.length; i++) {
            if (row[i].getElementsByClassName("lat") != null && row[i].getElementsByClassName("long") != null) {
                lat = parseFloat(row[i].getElementsByClassName("lat")[0].innerHTML);
                lng = parseFloat(row[i].getElementsByClassName("long")[0].innerHTML);
                console.log(lat);
                console.log(lng);
                var description = '<div id="content">' +
                    '<h1 id="firstHeading" class="firstHeading">' + row[i].getElementsByClassName("poster")[0].innerHTML + '</h1>' +
                    '<div id="bodyContent">' + row[i].getElementsByClassName("post")[0].innerHTML + '</div><br/>' +
                    '<div id="bodyTime">' + row[i].getElementsByClassName("time")[0].innerHTML + ' - ' + row[i].getElementsByClassName("source")[0].innerHTML + '</div>' + '</div>';
                var marker = new google.maps.Marker({
                    position: new google.maps.LatLng(lat, lng),
                    map: map,
                    icon: iconWithColor(usualColor),
                    title: row[i].getElementsByClassName("poster")[0].innerHTML,
                    desc: description,
                    shadow: shadow
                });
                oms.addMarker(marker);
                window.map = map;
            }
        }
    }


</script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAdsjlZu63hLWiK0D02VRC1cj2JdlDsgNU&callback=initMap" async
        defer></script>
</body>
</html>
