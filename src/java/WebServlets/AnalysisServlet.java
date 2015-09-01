package WebServlets;

import Data.CustomComparator;
import Data.StayPoint;
import org.json.simple.JSONObject;
import Data.User;
import Data.Utilities;
import DataBase.DataBase;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

@WebServlet(name = "AnalysisServlet", urlPatterns = {"/AnalysisServlet"})
public class AnalysisServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String user = null;
        String datFrom = null;
        String datTo = null;
        ArrayList<StayPoint> pointsList = null;
        double dmx = 500.0;
        double tmn = 20.0;
        double tmx = 500000000.0;
        double eprm = 0.1;
        int mnclstr = 5;
        Enumeration<String> parameterNames = request.getParameterNames();
        request.setCharacterEncoding("UTF-8");
        while (parameterNames.hasMoreElements()) {

            String paramName = parameterNames.nextElement();

            //out.write("<br>"+paramName+": ");
            if (paramName.equalsIgnoreCase("user")) {
                user = request.getParameter(paramName);
            } else if (paramName.equalsIgnoreCase("from")) {
                datFrom = request.getParameter(paramName);
            } else if (paramName.equalsIgnoreCase("to")) {
                datTo = request.getParameter(paramName);
            } else if (paramName.equalsIgnoreCase("dmx")) {
                dmx = Double.parseDouble(request.getParameter(paramName));
            } else if (paramName.equalsIgnoreCase("tmn")) {
                tmn = Double.parseDouble(request.getParameter(paramName));
            } else if (paramName.equalsIgnoreCase("tmx")) {
                tmx = Double.parseDouble(request.getParameter(paramName));
            } else if (paramName.equalsIgnoreCase("eprm")) {
                eprm = Double.parseDouble(request.getParameter(paramName));
            } else if (paramName.equalsIgnoreCase("mnclstr")) {
                mnclstr = Integer.parseInt(request.getParameter(paramName));
            }
            //out.write(request.getParameter(paramName).toString());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("y-M-d");
        Date start = null;
        Date end = null;

        try {
            start = sdf.parse(datFrom);
            end = sdf.parse(datTo);
        } catch (ParseException ex) {
            Logger.getLogger(VisualizationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        //out.write("<br>"+start.toString()+" "+end.toString());
        if (user.equals("everyone")) {
            ArrayList<User> users = DataBase.getAllUsers();
            ArrayList<StayPoint> stayPoints = new ArrayList<StayPoint>();
            if (users != null) {
                Collections.sort(users, new CustomComparator());
            }
            for (User u : users) {

                ArrayList<StayPoint> stPoints = u.calcStayPoints(tmx, tmn, dmx, 0.0);
                for (StayPoint sp : stPoints) {
                    stayPoints.add(sp);
                }
            }
            Map userAnalysisInfo = new HashMap();

            List<Cluster<DoublePoint>> clusters = Utilities.DBSCANClusterer(stayPoints, eprm, mnclstr);
            JSONArray list = new JSONArray();
            JSONArray list2 = new JSONArray();
            for (Cluster<DoublePoint> cl : clusters) {
                JSONArray list3 = new JSONArray();
                for (DoublePoint d : cl.getPoints()) {
                    JSONObject obj = new JSONObject();
                    obj.put("lat", d.getPoint()[0]);
                    obj.put("lon", d.getPoint()[1]);
                    list3.add(obj);
                }
                list2.add(list3);
                DoublePoint centroid = Utilities.getCentroid(cl);
                JSONObject obj = new JSONObject();
                obj.put("lat", centroid.getPoint()[0]);
                obj.put("lon", centroid.getPoint()[1]);
                list.add(obj);
            }
            userAnalysisInfo.put("interests", list);
            userAnalysisInfo.put("clusters", list2);
            out.write(JSONValue.toJSONString(userAnalysisInfo));
        } else {
            User tempUser = DataBase.getUser(user);
            if (tempUser != null) {

                Map userAnalysisInfo = new HashMap();

                //get all Aps from this user between this dates
                pointsList = tempUser.calcStayPoints(tmx, tmn, dmx, 0.0);
                //out.write("<br>size: "+apList.size());
                JSONArray list = new JSONArray();
                for (StayPoint rec : pointsList) {
                    JSONObject obj = new JSONObject();
                    obj.put("lat", rec.getLatitude());
                    obj.put("lon", rec.getLongitude());
                    list.add(obj);
                }

                userAnalysisInfo.put("spoints", list);

                out.write(JSONValue.toJSONString(userAnalysisInfo));
            }
        }

        out.close();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
