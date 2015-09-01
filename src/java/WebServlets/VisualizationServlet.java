package WebServlets;

import Data.AccessPoint;
import Data.BattRec;
import Data.CellRec;
import Data.GPSRec;
import org.json.simple.JSONObject;
import Data.User;
import Data.WifiRec;
import DataBase.DataBase;
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
import org.json.simple.JSONValue;

@WebServlet(name = "VisualizationServlet", urlPatterns = {"/VisualizationServlet"})
public class VisualizationServlet extends HttpServlet {

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
        ArrayList<WifiRec> apList = null;
        ArrayList<CellRec> cellList = null;
        ArrayList<BattRec> battList = null;
        ArrayList<GPSRec> gpsList = null;
        boolean aps = false;
        boolean cells = false;
        boolean batts = false;
        boolean gps = false;
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
            } else if (paramName.equalsIgnoreCase("aps") && request.getParameter(paramName).equals("true")) {
                aps = true;
            } else if (paramName.equalsIgnoreCase("cells") && request.getParameter(paramName).equals("true")) {
                cells = true;
            } else if (paramName.equalsIgnoreCase("batts") && request.getParameter(paramName).equals("true")) {
                batts = true;
            } else if (paramName.equalsIgnoreCase("gps") && request.getParameter(paramName).equals("true")) {
                gps = true;
            }
            //out.write(request.getParameter(paramName).toString());
        }
        
        if (user != null && datFrom != null && datTo != null) {
            
            User tempUser = DataBase.getUser(user);
            
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
            
            if (tempUser != null) {
                Map userVisualizationInfo = new HashMap();
                
                if (aps) {
                    //get all Aps from this user between this dates
                    apList = tempUser.getApRecordsUser(start, end);
                    //out.write("<br>size: "+apList.size());
                    JSONArray list1 = new JSONArray();
                    for (WifiRec rec : apList) {
                        JSONObject obj=new JSONObject();
                        obj.put("ssid",rec.getSSID());
                        obj.put("bssid",rec.getBSSID());
                        obj.put("lvl",rec.getLevel());
                        AccessPoint ap = DataBase.getAP(rec.getBSSID());
                        obj.put("lat",ap.getLatitude());
                        obj.put("lon",ap.getLongitude());
                        obj.put("freq",rec.getFrequency());
                               
                        obj.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(rec.getTimestamp()));
                        list1.add(obj);
                    }
                    userVisualizationInfo.put("aps", list1);
                }

                if (cells) {
                    //get all Base Stations from this user between this dates
                    cellList = tempUser.getCellRecordsUser(start, end);
                    //out.write("<br>size: "+apList.size());
                    JSONArray list2 = new JSONArray();
                    for (CellRec rec : cellList) {
                        JSONObject obj=new JSONObject();
                        obj.put("op",rec.getOperator());
                        obj.put("mmc",rec.getMmc());
                        obj.put("mnc",rec.getMnc());
                        obj.put("cid",rec.getCid());
                        obj.put("lat",rec.getLatitude());
                        obj.put("lon",rec.getLongitude());
                        obj.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(rec.getTimestamp()));
                        list2.add(obj);
                    }
                    userVisualizationInfo.put("cells", list2);
                }

                if (batts) {
                    //get all Battery Information from this user between this dates
                    battList = tempUser.getBattRecordsUser(start, end);
                    //out.write("<br>size: "+apList.size());
                    JSONArray list3 = new JSONArray();
                    for (BattRec rec : battList) {
                        JSONObject obj=new JSONObject();
                        obj.put("lvl",rec.getLevel());
                        obj.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(rec.getTimestamp()).replace(" ", "T"));
                        list3.add(obj);
                    }
                    userVisualizationInfo.put("batts", list3);
                }
                
                if (gps) {
                    //get all Battery Information from this user between this dates
                    gpsList = tempUser.getGPSRecordsUser(start, end);
                    //out.write("<br>size: "+apList.size());
                    JSONArray list4 = new JSONArray();
                    for (GPSRec rec : gpsList) {
                        JSONObject obj=new JSONObject();
                        obj.put("lat",rec.getLatitude());
                        obj.put("lon",rec.getLongitude());
                        obj.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(rec.getTimestamp()).replace(" ", "T"));
                        list4.add(obj);
                    }
                    userVisualizationInfo.put("gps", list4);
                }
                
                out.write(JSONValue.toJSONString(userVisualizationInfo));
            }
        } else {
            out.write("Error: parameters are: user, from, to");

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
