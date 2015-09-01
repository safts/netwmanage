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
            ArrayList<User> users = DataBase.getAllUsers();
            ArrayList<StayPoint> stayPoints = new ArrayList<StayPoint>();
            if (users != null) {
                Collections.sort(users, new CustomComparator());
            }
            for (User u : users){
//            User user1 = DataBase.getUser("user56");
               
                out.println("<div>--------------------------------------------</div>");
                out.println("<div>User: "+u.getId()+"</div>");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String dateInString = "2015-03-31 18:35:44";

                Date start = sdf.parse(dateInString);

                dateInString = "2015-08-31 18:35:44";

                Date end = sdf.parse(dateInString);

                ArrayList<StayPoint> stPoints = u.calcStayPoints(50000000.0,0.0,500000.0,0.0);
                for(StayPoint sp : stPoints){

                %>
                <div> Stay point lat= <%out.println(sp.getLatitude());%> , lon= <%out.println(sp.getLongitude());%></div>
                <div> Time enter= <%out.println(sp.getStart().toString());%> , leave= <%out.println(sp.getEnd().toString());%></div>
                <%
                    stayPoints.add(sp);
                }
                out.println("Total: "+stPoints.size()+"\n");
                out.println("<div>--------------------------------------------</div>");
            }
            
            List<Cluster<DoublePoint>> clusters = Utilities.DBSCANClusterer(stayPoints);
            out.println("<div>Clusters:\n</div>");
            for(Cluster<DoublePoint> cl : clusters){
                DoublePoint centroid = Utilities.getCentroid(cl);
                out.println("<div>Centroid: "+ centroid.getPoint()[0]+" , "
                        + centroid.getPoint()[1]+"</div>");
            }
        %>
    </body>
</html>
