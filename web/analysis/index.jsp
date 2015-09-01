<%@page import="Data.CustomComparator"%>
<%@page import="java.util.Collections"%>
<%@page import="DataBase.DataBase"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Data.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Analysis - Network Management 2015</title>
        <link rel="stylesheet" type="text/css" href="../css/style.css">
        <link rel="stylesheet" type="text/css" href="../css/toggle_style.css">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
        <script src="../js/markerclusterer_compiled.js"></script>
        <script src="../js/convex_hull.js"></script>
        <!--<link href="../css/animate.css" rel="stylesheet" media="screen">-->
        <script>
            var stay_point_image = {
                url: '../images/stay-point-marker.png',
                // This marker is 20 pixels wide by 32 pixels tall.
                size: new google.maps.Size(40, 51),
                // The origin for this image is 0,0.
                origin: new google.maps.Point(0, 0),
                // The anchor for this image is the base of the flagpole at 0,32.
                anchor: new google.maps.Point(20, 51)
            };

            var interest_point_image = {
                url: '../images/interest-point-marker.png',
                // This marker is 20 pixels wide by 32 pixels tall.
                size: new google.maps.Size(40, 51),
                // The origin for this image is 0,0.
                origin: new google.maps.Point(0, 0),
                // The anchor for this image is the base of the flagpole at 0,32.
                anchor: new google.maps.Point(20, 51)
            };
        </script>
    </head>
    <body>
        <%//@ include file="/utilities/pageStart.jsp"%>
        <div id="footer">Copyright © 2015 - S. Aytsidis - A. Giannakopoulos</div>
        <div id="head"> 
            <a id="back-button" href="../">< Go Back</a>
            <div style="margin-left: 200px;">
                <div id="toggle-box">
                    <div class="toggle-title">D<span style="font-size: 9px;">max</span> parameter</div>
                    <label class="switch">
                        <input type="text" id="dmx" name="dmx" style="width: 80px;" value="10.0" onchange="getData('none')">
                    </label>
                </div>
                <div id="toggle-box">
                    <div class="toggle-title">T<span style="font-size: 9px;">min</span> parameter</div>
                    <label class="switch">
                        <input type="text" id="tmn" name="tmn" style="width: 80px;" value="60.0" onchange="getData('none')">
                    </label>
                </div>
                <div id="toggle-box">
                    <div class="toggle-title">T<span style="font-size: 9px;">max</span> parameter</div>
                    <label class="switch">
                        <input type="text" id="tmx" name="tmx" style="width: 80px;" value="100000.0" onchange="getData('none')">
                    </label>
                </div>
                <div id="toggle-box">
                    <div class="toggle-title">e parameter</div>
                    <label class="switch">
                        <input type="text" id="eprm" name="eprm" style="width: 80px;" value="0.1" onchange="getData('none')">
                    </label>
                </div>
                <div id="toggle-box">
                    <div class="toggle-title">Min cluster</div>
                    <label class="switch">
                        <input type="text" id="mnclstr" name="mnclstr" style="width: 80px;" value="5" onchange="getData('none')">
                    </label>
                </div>
            </div>
            <div id="msg"></div>
        </div>
        <div id="alerts"></div>
        <div id="container">
            <div id="sidebar">
                <div id="date-pick" style="margin-top: 5px;">
                    <label for="from">From:</label>
                    <input type="text" id="from" name="from" style="width: 180px;" value="2015-03-01" onchange="getData('none')">
                    <label for="to">To:</label>
                    <input type="text" id="to" name="to" style="width: 180px;" value="2015-05-06" onchange="getData('none')">
                </div>
                <ul style="overflow-y:hidden; margin-bottom: -9px;">
                    <li><a href="#" id="everyone" onclick="getData(this.id)">Everyone</a></li>
                </ul>
                <ul class="analyze" style="list-style-type:none">
                    <%
                        ArrayList<User> users = DataBase.getAllUsers();
                        if (users != null) {
                            Collections.sort(users, new CustomComparator());
                            for (User u : users) {
                    %>
                    <li><a href="#" id="<%=u.getId()%>" onclick="getData(this.id)"><%=u.getId()%></a></li>
                        <%
                                }
                            }
                        %>
                </ul>
            </div>
            <div id="map-canvas" style="width: calc(100% - 200px);height:100%; margin: 0px; padding: 0px;float: left;"></div>
        </div>

        <script>
            var map;
            var jsonPost = null;
            var markers_sp = [];
            var markers_pi = [];
            var polygons_pi = [];
            var bounds;
            var last_user;
            var state = ['none' + '2015-03-01' + '2015-04-29', 'none' + '2015-03-01' + '2015-04-29'];
            var msg = "";
            var alert = null;

            function toggleMouseWait() {
                $("body").toggleClass("wait");
                return false;
            }

            function updateVals(arr, newVal) {
                arr[0] = arr[1];
                arr[1] = newVal;
            }

            function getData(id) {
                $("#" + last_user).removeClass('hover');
                var stateChanged = false;
                var getDataFromServer = false;
                if (id !== 'none') {
                    stateChanged = true;
                    updateVals(state, id + $("#from").val() + $("#to").val() + $("#dmx").val() + $("#tmn").val() + $("#tmx").val() + $("#eprm").val() + $("#mnclstr").val());
                    last_user = id;
                } else if (last_user + $("#from").val() + $("#to").val() !== state[1]) {
                    stateChanged = true;
                    updateVals(state, last_user + $("#from").val() + $("#to").val() + $("#dmx").val() + $("#tmn").val() + $("#tmx").val() + $("#eprm").val() + $("#mnclstr").val());
                }
                $("#" + last_user).addClass('hover');

                //cache handle
                jsonPost = {};
                jsonPost.user = last_user;
                jsonPost.from = $("#from").val();
                jsonPost.to = $("#to").val();
                jsonPost.dmx = $("#dmx").val();
                jsonPost.tmn = $("#tmn").val();
                jsonPost.tmx = $("#tmx").val();
                jsonPost.eprm = $("#eprm").val();
                jsonPost.mnclstr = $("#mnclstr").val();
                if (stateChanged && state[0] !== state[1]) {
                    console.log("new state");
                    getDataFromServer = true;
                }
                if (stateChanged && state[0] !== state[1]) {
                    console.log(state);
                    bounds = new google.maps.LatLngBounds();
                    setAllMap(null, markers_sp);
                    markers_sp = [];
                    setAllMap(null, markers_pi);
                    markers_pi = [];
                    setAllMap(null, polygons_pi);
                    polygons_pi = [];
                }
                msg = last_user + ": ";
                if (getDataFromServer) {
                    toggleMouseWait()
                    $.getJSON("../AnalysisServlet", jsonPost)
                            .done(function (json) {
                                if ('spoints' in json) {
                                    $.each(json.spoints, function (key, value) {
                                        marker = new google.maps.Marker({
                                            position: new google.maps.LatLng(value.lat, value.lon),
                                            icon: stay_point_image
                                        });
                                        markers_sp.push(marker);
                                        bounds.extend(new google.maps.LatLng(value.lat, value.lon));
                                    });
                                }
                                if ('interests' in json) {
                                    setAllMap(null, markers_pi);
                                    $.each(json.interests, function (key, value) {
                                        marker = new google.maps.Marker({
                                            position: new google.maps.LatLng(value.lat, value.lon),
                                            icon: interest_point_image
                                        });
                                        markers_pi.push(marker);
                                        bounds.extend(new google.maps.LatLng(value.lat, value.lon));
                                    });
                                }
                                if ('clusters' in json) {
                                    setAllMap(null, polygons_pi);
                                    for (var i = 0; i < json.clusters.length; i++) {
                                        var cluster = json.clusters[i];
                                        var triangleCoords = [];
                                        for (var j = 0; j < cluster.length; j++) {
                                            triangleCoords.push(new google.maps.LatLng(cluster[j].lat, cluster[j].lon));
                                        }
                                        triangleCoords.sort(sortPointY);
                                        triangleCoords.sort(sortPointX);
                                        
                                        var hullPoints = [];
                                        chainHull_2D( triangleCoords, triangleCoords.length, hullPoints );
                                        // Construct the polygon.
                                        var bermudaTriangle = new google.maps.Polygon({
                                            paths: triangleCoords,
                                            strokeColor: '#FF0000',
                                            strokeOpacity: 0.8,
                                            strokeWeight: 2,
                                            fillColor: '#FF0000',
                                            fillOpacity: 0.35
                                        });
                                        polygons_pi.push(bermudaTriangle);
                                    }
                                }
                                drawAll();
                                toggleMouseWait()
                            });
                } else {
                    drawAll();
                }
            }

            function sortPointX(a, b) {
                return a.lng() - b.lng();
            }
            function sortPointY(a, b) {
                return a.lat() - b.lat();
            }

            function setAllMap(map, markers) {
                for (var i = 0; i < markers.length; i++) {
                    markers[i].setMap(map);
                }
            }

            function drawAll() {
                msg = last_user + ": ";
                if (markers_pi.length + markers_sp.length !== 0)
                    map.fitBounds(bounds);
                if (last_user === 'everyone') {
                    msg += "visited " + markers_pi.length + " points";
                    showAllert(msg);
                    setAllMap(map, markers_pi);
                    setAllMap(map, polygons_pi);
                } else {
                    msg += "stayed at " + markers_sp.length + " points ";
                    showAllert(msg);
                    setAllMap(map, markers_sp);
                }
            }

            function showAllert(string) {
                $('#alerts').show().removeClass('animated bounceOutUp').addClass('animated bounceInDown');
                $("#alerts").html("<span>" + string + "</span>");
                if (alert != null) {
                    clearTimeout(alert);
                }
                alert = setTimeout(function () {
                    $('#alerts').removeClass('animated bounceInDown').addClass('animated bounceOutUp');
                }, 9000);
            }

            function initialize() {
                geocoder = new google.maps.Geocoder();
                var mapOptions = {
                    zoom: 10,
                    streetViewControl: false,
                    mapTypeControl: false,
                    mapTypeControlOptions: {
                        style: google.maps.MapTypeControlStyle.DEFAULT
                    },
                    scaleControl: false,
                    navigationControl: false,
                    navigationControlOptions: {
                        style: google.maps.NavigationControlStyle.DEFAULT
                    },
                    keyboardShortcuts: false
                    ,
                    center: new google.maps.LatLng(37.975665, 23.733802),
                    mapTypeId: google.maps.MapTypeId.ROADMAP,
                };
                map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
            }

            google.maps.event.addDomListener(window, 'load', initialize);

            $(function () {
                $("#from").datepicker({
                    defaultDate: new Date(2015, 3 - 3, 26),
                    dateFormat: "yy-mm-dd",
                    changeMonth: true,
                    numberOfMonths: 2,
                    onClose: function (selectedDate) {
                        $("#to").datepicker("option", "minDate", selectedDate);
                    }
                });
                $("#to").datepicker({
                    defaultDate: new Date(2015, 3 - 3, 26),
                    dateFormat: "yy-mm-dd",
                    changeMonth: true,
                    numberOfMonths: 2,
                    onClose: function (selectedDate) {
                        $("#from").datepicker("option", "maxDate", selectedDate);
                    }
                });
            });


        </script>
        <%//@ include file="/utilities/pageEnd.jsp" %>
    </body>
</html>
