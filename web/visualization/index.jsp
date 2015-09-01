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
        <title>Visualization - Network Management 2015</title>
        <link rel="stylesheet" type="text/css" href="../css/style.css">
        <link rel="stylesheet" type="text/css" href="../css/toggle_style.css">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
        <script src="../js/markerclusterer_compiled.js"></script>
        <!--<link href="../css/animate.css" rel="stylesheet" media="screen">-->
        <script>
            var styles = [[{
                        url: '../images/wf2.png',
                        height: 55,
                        width: 56,
                        anchor: [28, 0],
                        textColor: '#ff00ff',
                        textSize: 10
                    }, {
                        url: '../images/wf3.png',
                        height: 65,
                        width: 66,
                        anchor: [33, 0],
                        textColor: '#ff0000',
                        textSize: 11
                    }, {
                        url: '../images/wf4.png',
                        height: 77,
                        width: 78,
                        anchor: [41, 0],
                        textColor: '#ff0000',
                        textSize: 11
                    }, {
                        url: '../images/wf5.png',
                        height: 89,
                        width: 90,
                        anchor: [45, 0],
                        textColor: '#ffffff',
                        textSize: 12
                    }], [{
                        url: '../images/cl2.png',
                        height: 55,
                        width: 56,
                        anchor: [28, 0],
                        textColor: '#ff00ff',
                        textSize: 10
                    }, {
                        url: '../images/cl3.png',
                        height: 65,
                        width: 66,
                        anchor: [33, 0],
                        textColor: '#ff0000',
                        textSize: 11
                    }, {
                        url: '../images/cl4.png',
                        height: 77,
                        width: 78,
                        anchor: [41, 0],
                        textColor: '#ff0000',
                        textSize: 11
                    }, {
                        url: '../images/cl5.png',
                        height: 89,
                        width: 90,
                        anchor: [45, 0],
                        textColor: '#ffffff',
                        textSize: 12
                    }]];

            var wifi_image = {
                url: '../images/wifi-marker.png',
                // This marker is 20 pixels wide by 32 pixels tall.
                size: new google.maps.Size(40, 51),
                // The origin for this image is 0,0.
                origin: new google.maps.Point(0, 0),
                // The anchor for this image is the base of the flagpole at 0,32.
                anchor: new google.maps.Point(20, 51)
            };

            var gps_image = {
                url: '../images/gps-marker.png',
                // This marker is 20 pixels wide by 32 pixels tall.
                size: new google.maps.Size(35, 51),
                // The origin for this image is 0,0.
                origin: new google.maps.Point(0, 0),
                // The anchor for this image is the base of the flagpole at 0,32.
                anchor: new google.maps.Point(17, 51)
            };

            var cell_image = {
                url: '../images/cell-marker.png',
                // This marker is 20 pixels wide by 32 pixels tall.
                size: new google.maps.Size(36, 61),
                // The origin for this image is 0,0.
                origin: new google.maps.Point(0, 0),
                // The anchor for this image is the base of the flagpole at 0,32.
                anchor: new google.maps.Point(18, 61)
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
                    <div class="toggle-title">Wifi Aps</div>
                    <label class="switch">
                        <input type="checkbox" id="aps" class="switch-input" checked onclick="getData('none')">
                        <span class="switch-label" data-on="On" data-off="Off"></span>
                        <span class="switch-handle"></span>
                    </label>
                </div>
                <div id="toggle-box">
                    <div class="toggle-title">Gps path</div>
                    <label class="switch">
                        <input type="checkbox" id="gps" class="switch-input" onclick="getData('none')">
                        <span class="switch-label" data-on="On" data-off="Off"></span>
                        <span class="switch-handle"></span>
                    </label>
                </div>
                <div id="toggle-box">
                    <div class="toggle-title">Battery Stats</div>
                    <label class="switch">
                        <input type="checkbox" id="batts" class="switch-input" onclick="getData('none')">
                        <span class="switch-label" data-on="On" data-off="Off"></span>
                        <span class="switch-handle"></span>
                    </label>
                </div>
                <div id="toggle-box">
                    <div class="toggle-title">Cells</div>
                    <label class="switch">
                        <input type="checkbox" id="cells" class="switch-input" onclick="getData('none')">
                        <span class="switch-label" data-on="On" data-off="Off"></span>
                        <span class="switch-handle"></span>
                    </label>
                </div>
            </div>
            <div id="msg"></div>
        </div>
        <div id="alerts"></div>
        <div id="battery-chart" style="display:none;">
            <div id="linechart_material"></div>
        </div>
        <div id="container">
            <div id="sidebar">
                <div id="date-pick" style="margin-top: 5px;">
                    <label for="from">From:</label>
                    <input type="text" id="from" name="from" style="width: 180px;" value="2015-03-01" onchange="getData('none')">
                    <label for="to">To:</label>
                    <input type="text" id="to" name="to" style="width: 180px;" value="2015-05-06" onchange="getData('none')">
                </div>
                <ul class="visualize" style="list-style-type:none">
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
            google.load('visualization', '1.1', {packages: ['line']});

            var map;
            var chart;
            var jsonPost = null;
            var markers_wf = [];
            var markers_cl = [];
            var markers_gps = [];
            var bounds;
            var userGpsPath = null;
            var last_user;
            var lineChartData;
            var lineChartRows = [];
            var markerClusterer_wf = null;
            var markerClusterer_cl = null;
            var state = ['none' + '2015-03-01' + '2015-04-29', 'none' + '2015-03-01' + '2015-04-29'];
            var apsVal = true;
            var gpsVal = false;
            var batVal = false;
            var celVal = false;
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
                hideStats();
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
                apsVal = document.getElementById("aps").checked;
                gpsVal = document.getElementById("gps").checked;
                batVal = document.getElementById("batts").checked;
                celVal = document.getElementById("cells").checked;
                if (stateChanged && state[0] !== state[1]) {
                    console.log("new state");
                    getDataFromServer = true;
                    jsonPost.aps = document.getElementById("aps").checked;
                    jsonPost.gps = document.getElementById("gps").checked;
                    jsonPost.batts = document.getElementById("batts").checked;
                    jsonPost.cells = document.getElementById("cells").checked;
                } else if (state[0] !== state[1]) {
                    console.log("old state");
                    if (apsVal && markers_wf.length === 0) {
                        getDataFromServer = true;
                        jsonPost.aps = apsVal;
                    }
                    if (gpsVal && markers_gps.length === 0) {
                        getDataFromServer = true;
                        jsonPost.gps = gpsVal;
                    }
                    if (batVal && lineChartRows.length === 0) {
                        getDataFromServer = true;
                        jsonPost.batts = batVal;
                    }
                    if (celVal && markers_cl.length === 0) {
                        getDataFromServer = true;
                        jsonPost.cells = celVal;
                    }
                }
                if (stateChanged && state[0] !== state[1]) {
                    console.log(state);
                    bounds = new google.maps.LatLngBounds();
                    markers_wf = [];
                    markers_cl = [];
                    markers_gps = [];
                    lineChartRows = [];
                    if (userGpsPath)
                        userGpsPath.setMap(null);
                }
                if (markerClusterer_wf) {
                    markerClusterer_wf.clearMarkers();
                }
                if (markerClusterer_cl) {
                    markerClusterer_cl.clearMarkers();
                }
                if (getDataFromServer) {
                    toggleMouseWait();
                    $.getJSON("../VisualizationServlet", jsonPost)
                            .done(function (json) {
                                if ('aps' in json) {
                                    $.each(json.aps, function (key, value) {
                                        marker = new google.maps.Marker({
                                            position: new google.maps.LatLng(value.lat, value.lon),
                                            icon: wifi_image,
                                            title: value.ssid});
                                        var content = '<div id="info-content">' +
                                                '<div id="siteNotice">' +
                                                '</div>' +
                                                '<h2 id="firstHeading" class="firstHeading">' + value.ssid + '</h2>' +
                                                '<div id="bodyContent">' +
                                                '<b>MAC</b> ' + value.bssid + '<br>' + '<b>Chanel</b> ' + value.freq + '<br>' + '<b>latitude</b> ' + value.lat + '<br>' + '<b>longitude</b> ' + value.lon + '<br>' + '<b>level</b> ' + value.lvl + '' +
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
                                        bounds.extend(new google.maps.LatLng(value.lat, value.lon));
                                        markers_wf.push(marker);
                                    });
                                }
                                if ('gps' in json) {
                                    $.each(json.gps, function (key, value) {
                                        bounds.extend(new google.maps.LatLng(value.lat, value.lon));
                                        markers_gps.push(new google.maps.LatLng(value.lat, value.lon));
                                    });
                                }
                                if ('batts' in json) {
                                    var steps = 1;
                                    if (json.batts.length > 100) {
                                        steps = parseInt(json.batts.length / 100);
                                    }
                                    var count = 1;
                                    $.each(json.batts, function (key, value) {
                                        if (count % steps === 0) {
                                            lineChartRows.push([new Date(value.time), value.lvl]);
                                        }
                                        count++;
                                    });
                                }
                                if ('cells' in json) {
                                    $.each(json.cells, function (key, value) {
                                        marker = new google.maps.Marker({
                                            position: new google.maps.LatLng(value.lat, value.lon),
                                            icon: cell_image});
                                        var content = '<div id="info-content">' +
                                                '<div id="siteNotice">' +
                                                '</div>' +
                                                '<h2 id="firstHeading" class="firstHeading">' + value.op + '</h2>' +
                                                '<div id="bodyContent">' +
                                                '<b>Mmc</b> ' + value.mmc + '<br>' + '<b>Mnc</b> ' + value.mnc + '<br>' + '<b>Cid</b> ' + value.cid + '<br>' + '<b>latitude</b> ' + value.lat + '<br>' + '<b>longitude</b> ' + value.lon + '' +
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
                                        markers_cl.push(marker);
                                    });
                                }
                                drawAll();
                                toggleMouseWait();
                            });
                } else {
                    drawAll();
                }
            }

            function drawAll() {
                msg = last_user + ": ";
                if (markers_wf.length + markers_cl.length + markers_gps.length !== 0)
                    map.fitBounds(bounds);
                if (apsVal) {
                    msg += "scaned " + markers_wf.length + " wifi's ";
                    markerClusterer_wf = new MarkerClusterer(map, markers_wf, {
                        maxZoom: 17,
                        styles: styles[0]
                    });
                }
                if (celVal) {
                    if (apsVal)
                        msg += " and ";
                    msg += "founded " + markers_cl.length + " Base Stations";
                    markerClusterer_cl = new MarkerClusterer(map, markers_cl, {
                        maxZoom: 17,
                        styles: styles[1]
                    });
                }
                if (gpsVal) {
                    if (userGpsPath)
                        userGpsPath.setMap(null);
                    userGpsPath = new google.maps.Polyline({
                        path: markers_gps,
                        geodesic: true,
                        strokeColor: '#FF0000', strokeOpacity: 1.0,
                        strokeWeight: 2
                    });
                    userGpsPath.setMap(map);
                } else {
                    if (userGpsPath)
                        userGpsPath.setMap(null);
                }
                if (batVal) {
                    showStats();
                } else {
                    hideStats();
                }
                if (apsVal || gpsVal)
                    showAllert(msg);
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

            function showStats() {
                document.getElementById("battery-chart").style.display = "block";
                lineChartData = new google.visualization.DataTable();
                lineChartData.addColumn('date', 'Days');
                lineChartData.addColumn('number', 'Battery level per Days pass');
                lineChartData.addRows(lineChartRows);
                chart = new google.charts.Line(document.getElementById('linechart_material'));

                chart.draw(lineChartData, {
                    chart: {
                        title: 'Battery level per Days pass'
                    },
                    width: 600,
                    height: 450
                });
            }

            function hideStats() {
                document.getElementById("battery-chart").style.display = "none";
            }

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
