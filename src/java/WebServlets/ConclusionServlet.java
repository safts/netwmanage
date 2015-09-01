/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServlets;

import Data.AccessPoint;
import Data.GPSRec;
import Data.User;
import Data.WifiRec;
import DataBase.DataBase;
import DataBase.DataBase.NetworkInfo;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "ConclusionServlet", urlPatterns = {"/ConclusionServlet"})
public class ConclusionServlet extends HttpServlet {

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
        String mode = "";
        ArrayList<AccessPoint> apList = null;
        ArrayList<GPSRec> gpsList = null;

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
            } else if (paramName.equalsIgnoreCase("mode")) {
                mode = request.getParameter(paramName);
            }
            //out.write(request.getParameter(paramName).toString());
        }

        if (mode.equals("low_bat")) {
            int[] lowBat = DataBase.getLowBattery();
            Map userVisualizationInfo = new HashMap();
            JSONArray list1 = new JSONArray();
            int j = 0;
            for (int i : lowBat) {
                JSONObject obj = new JSONObject();
                obj.put("hour", j);
                obj.put("sum", i);
                list1.add(obj);
                j++;
//                System.out.println("Hour bat: "+i);
            }
            userVisualizationInfo.put("low_bat", list1);
            out.write(JSONValue.toJSONString(userVisualizationInfo));
        } else if (mode.equals("bat_save")) {
            SimpleDateFormat sdf = new SimpleDateFormat("y-M-d");
            Date start = null;
            Date end = null;

            try {
                start = sdf.parse(datFrom);
                end = sdf.parse(datTo);
            } catch (ParseException ex) {
                Logger.getLogger(VisualizationServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            User tempUser = DataBase.getUser(user);

            if (tempUser != null) {
                Map userVisualizationInfo = new HashMap();
                
                gpsList = tempUser.getGPSRecordsUser(start, end);
                //out.write("<br>size: "+apList.size());
                JSONArray list4 = new JSONArray();
                for (GPSRec rec : gpsList) {
                    JSONObject obj = new JSONObject();
                    obj.put("lat", rec.getLatitude());
                    obj.put("lon", rec.getLongitude());
                    obj.put("time", rec.getTimestamp().toString().replace(" ", "T"));
                    list4.add(obj);
                }
                userVisualizationInfo.put("gps", list4);

                //get all Aps from this user between this dates
                apList = tempUser.getClosestAPs(gpsList);
                //out.write("<br>size: "+apList.size());
                JSONArray list1 = new JSONArray();
                for (AccessPoint rec : apList) {
                    JSONObject obj = new JSONObject();
                    obj.put("ssid", rec.getSSID());
                    obj.put("bssid", rec.getBSSID());
                    obj.put("lvl", rec.getLevel());
                    obj.put("lat", rec.getLatitude());
                    obj.put("lon", rec.getLongitude());
                    obj.put("freq", rec.getFrequency());
                    list1.add(obj);
                }
                userVisualizationInfo.put("aps", list1);

                out.write(JSONValue.toJSONString(userVisualizationInfo));
            }

        } else if (mode.equals("carriers")) {
            ArrayList<NetworkInfo> carriers = DataBase.getNetworkStats();
            Map userVisualizationInfo = new HashMap();
            JSONArray list1 = new JSONArray();
            for (NetworkInfo n : carriers) {
                JSONObject obj = new JSONObject();
                obj.put("carr", n.carrier);
                obj.put("users", n.number);
                list1.add(obj);
            }
            userVisualizationInfo.put("carriers", list1);

            out.write(JSONValue.toJSONString(userVisualizationInfo));
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
