<%@page import="DataBase.DataBase.NetworkInfo"%>
<%@page import="org.apache.commons.math3.ml.clustering.Cluster"%>
<%@page import="org.apache.commons.math3.ml.clustering.DoublePoint"%>
<%@page import="org.apache.commons.math3.ml.clustering.DoublePoint"%>
<%@page import="Data.Utilities"%>
<%@page import="java.util.List"%>
<%@page import="Data.CustomComparator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Collections"%>
<%@page import="Data.StayPoint"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="Data.User"%>
<%@page import="DataBase.buildDB"%>
<%@page import="DataBase.DataBase"%>

<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>TODO supply a title</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div>TODO write content</div>
        <%
            int [] lowBat = DataBase.getLowBattery();
            int j=0;
            for(int i : lowBat){
                out.println("<div>At "+j+":00 there were "+i+"</div>");
                j++;
            }
            
            ArrayList<NetworkInfo> carriers = DataBase.getNetworkStats();
            for(NetworkInfo n : carriers){
                out.println("<div>Carrier \""+n.carrier+"\" has "
                        +n.number +" subscribers</div>");
            }
        %>
    </body>
</html>
