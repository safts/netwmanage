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
        <title>Conclusion - Network Management 2015</title>
        <link rel="stylesheet" type="text/css" href="../css/style.css">
        <link rel="stylesheet" type="text/css" href="../css/toggle_style.css">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
        <script src="../js/markerclusterer_compiled.js"></script>
        <script>
            var wifi_image = {
                url: '../images/wifi-marker.png',
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
            <div id="msg"></div>
            <div id="alert"></div>
        </div>
        <div id="container" class="conclusion">


            <h2 class="conclusion">Bar Diagram that shows the users that every hour had less than 15% of full battery.</h2>
            <div id="bar-diagram-battery">
                <div id="columnchart_material1"></div>
            </div>


            <h2 class="conclusion">User battery saving: wifi hot spots he could connect at his path.</h2>
            <div id="battery-saving">
                <div id="sidebar">
                    <div id="date-pick" style="margin-top: 5px;">
                        <label for="from">From:</label>
                        <input type="text" id="from" name="from" style="width: 180px;" value="2015-03-01" onchange="getSaveData('none')">
                        <label for="to">To:</label>
                        <input type="text" id="to" name="to" style="width: 180px;" value="2015-05-06" onchange="getSaveData('none')">
                    </div>
                    <ul class="visualize" style="list-style-type:none">
                        <%
                            ArrayList<User> users = DataBase.getAllUsers();
                            if (users != null) {
                                Collections.sort(users, new CustomComparator());
                                for (User u : users) {
                        %>
                        <li><a href="#" id="<%=u.getId()%>" onclick="getSaveData(this.id)"><%=u.getId()%></a></li>
                            <%
                                    }
                                }
                            %>
                    </ul>
                </div>
                <div id="map-canvas" style="width: calc(100% - 200px);height:100%; margin: 0px; padding: 0px;float: left;"></div>
            </div>


            <h2 class="conclusion">Bar Diagram that shows how many user each carrier has.</h2>
            <div id="bar-diagram-carrier">
                <div id="columnchart_material2"></div>
            </div>

        </div>

        <script>
            google.load("visualization", "1.1", {packages: ["bar"]});

            var map;
            var jsonPost = null;
            var markers_wf = [];
            var markers_gps = [];
            var userGpsPath = null;
            var batData = [];
            var batDataCached = false;
            var carriersData = [];
            var carriersDataCached = false;
            var bounds;
            var last_user;
            var state = ['none' + '2015-03-01' + '2015-04-29', 'none' + '2015-03-01' + '2015-04-29'];

            function toggleMouseWait() {
                $("body").toggleClass("wait");
                return false;
            }

            function getLowBatData() {
                jsonPost = {};
                jsonPost.mode = "low_bat";
                if (batDataCached === false) {
                    toggleMouseWait();
                    $.getJSON("../ConclusionServlet", jsonPost)
                            .done(function (json) {
                                batData.push(['Day Hours', '#Users']);
                                if ('low_bat' in json) {
                                    $.each(json.low_bat, function (key, value) {
                                        batData.push([value.hour + ":00", value.sum]);
                                    });
                                }
                                drawLowBatChart();
                                toggleMouseWait();
                            });
                }
            }

            function drawLowBatChart() {
                var options = {
                    chart: {
                        title: 'Users per Hour with less than 15% Battery',
                        subtitle: 'Sum of users',
                    },
                    width: 800,
                    height: 500
                };

                var data = google.visualization.arrayToDataTable(batData);
                var chart = new google.charts.Bar(document.getElementById('columnchart_material1'));

                chart.draw(data, options);
                batDataCached = true;
            }

            function getCarriersData() {
                jsonPost = {};
                jsonPost.mode = "carriers";
                if (carriersDataCached === false) {
                    toggleMouseWait();
                    $.getJSON("../ConclusionServlet", jsonPost)
                            .done(function (json) {
                                carriersData.push(['Carriers', '#Users']);
                                if ('carriers' in json) {
                                    $.each(json.carriers, function (key, value) {
                                        carriersData.push([value.carr, value.users]);
                                    });
                                }
                                drawCarriersChart();
                                toggleMouseWait();
                            });
                }
            }

            function drawCarriersChart() {
                var options = {
                    chart: {
                        title: 'Users per Carrier',
                        subtitle: 'Sum of users',
                    },
                    width: 800,
                    height: 500
                };

                var data = google.visualization.arrayToDataTable(carriersData);
                var chart = new google.charts.Bar(document.getElementById('columnchart_material2'));

                chart.draw(data, options);
                carriersDataCached = true;
            }

            setTimeout(function () {
                getLowBatData();
                getCarriersData();
            }, 2000);

            function updateVals(arr, newVal) {
                arr[0] = arr[1];
                arr[1] = newVal;
            }

            function getSaveData(id) {
                $("#" + last_user).removeClass('hover');
                var stateChanged = false;
                var getDataFromServer = false;
                if (id !== 'none') {
                    stateChanged = true;
                    updateVals(state, id + $("#from").val() + $("#to").val());
                    last_user = id;
                } else if (last_user + $("#from").val() + $("#to").val() !== state[1]) {
                    stateChanged = true;
                    updateVals(state, last_user + $("#from").val() + $("#to").val());
                }
                $("#" + last_user).addClass('hover');

                //cache handle
                jsonPost = {};
                jsonPost.user = last_user;
                jsonPost.from = $("#from").val();
                jsonPost.to = $("#to").val();
                jsonPost.mode = "bat_save";
                if (stateChanged && state[0] !== state[1]) {
                    console.log("new state");
                    getDataFromServer = true;
                }
                if (stateChanged && state[0] !== state[1]) {
                    console.log(state);
                    bounds = new google.maps.LatLngBounds();
                    setAllMap(null, markers_wf);
                    markers_wf = [];
                    markers_gps = [];
                    if (userGpsPath)
                        userGpsPath.setMap(null);
                }
                if (getDataFromServer) {
                    toggleMouseWait();
                    $.getJSON("../ConclusionServlet", jsonPost)
                            .done(function (json) {
                                if ('aps' in json) {
                                    $.each(json.aps, function (key, value) {
                                        marker = new google.maps.Marker({
                                            position: new google.maps.LatLng(value.lat, value.lon),
                                            icon: wifi_image,
                                            map: map,
                                            title: value.ssid
                                        });
                                        var content = '<div id="info-content">' +
                                                '<div id="siteNotice">' +
                                                '</div>' +
                                                '<h2 id="firstHeading" class="firstHeading">' + value.ssid + '</h2>' +
                                                '<div id="bodyContent">' +
                                                '<b>MAC</b> ' + value.bssid + '<br>' +
                                                '<b>Chanel</b> ' + value.freq + '<br>' +
                                                '<b>latitude</b> ' + value.lat + '<br>' +
                                                '<b>longitude</b> ' + value.lon + '<br>' +
                                                '<b>level</b> ' + value.lvl + '' +
                                                '</div>' +
                                                '</div>';
                                        var infowindow = new google.maps.InfoWindow()
                                        google.maps.event.addListener(marker, 'mouseover', (function (marker, content, infowindow) {
                                            return function () {
                                                infowindow.setContent(content);
                                                infowindow.open(map, marker);
                                            };
                                        })(marker, content, infowindow));
                                        google.maps.event.addListener(marker, 'mouseout', (function (marker, content, infowindow) {
                                            return function () {
                                                infowindow.close();
                                            };
                                        })(marker, content, infowindow));
                                        markers_wf.push(marker);
                                        bounds.extend(new google.maps.LatLng(value.lat, value.lon));
                                    });
                                }
                                if ('gps' in json) {
                                    $.each(json.gps, function (key, value) {
                                        bounds.extend(new google.maps.LatLng(value.lat, value.lon));
                                        markers_gps.push(new google.maps.LatLng(value.lat, value.lon));
                                    });
                                }
                                drawAll();
                                toggleMouseWait();
                            });
                }
            }

            function setAllMap(map, markers) {
                for (var i = 0; i < markers.length; i++) {
                    markers[i].setMap(map);
                }
            }

            function drawAll() {
                if (markers_wf.length + markers_gps.length !== 0)
                    map.fitBounds(bounds);
                if (userGpsPath)
                    userGpsPath.setMap(null);
                userGpsPath = new google.maps.Polyline({
                    path: markers_gps,
                    geodesic: true,
                    strokeColor: '#FF0000', strokeOpacity: 1.0,
                    strokeWeight: 2
                });
                userGpsPath.setMap(map);
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
