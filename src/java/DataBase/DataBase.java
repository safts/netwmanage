package DataBase;

import Data.AccessPoint;
import Data.User;
import Data.WifiRec;
import Data.BattRec;
import Data.CellRec;
import Data.CustomComparator;
import Data.GPSRec;
import Data.StayPoint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.math3.stat.clustering.DBSCANClusterer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sergios
 */

public class DataBase {
    
    
    private static HashMap<String,User> users;
    
    private static HashMap<String,AccessPoint> APs;
    private static HashMap<String,GPSRec> gpsRecs;
    private static HashMap<String,CellRec> cellRecs;
    private static HashMap<String,BattRec> battRecs;
    
    private static boolean gpsRead;
    private static boolean cellRead;
    private static boolean battRead;
    private static boolean apsRead;
    
    public DataBase(){
        users = new HashMap<String,User>();
        APs = new HashMap<String,AccessPoint>();
        gpsRecs = new HashMap<String,GPSRec>();
        cellRecs = new HashMap<String,CellRec>();
        battRecs = new HashMap<String,BattRec>();
        
        gpsRead = false;
        cellRead = false;
        battRead = false;
        apsRead = false;
    }
    
    public static boolean wifiRead(){
        return apsRead;
    }
    
    public static void readWifi(){
        if(apsRead ==false){
            buildDB.insertWIFI();
            apsRead = true;
        }
    }
    
    public static boolean gpsRead(){
        return gpsRead;
    }
    
    public static void readGPS(){
        if(gpsRead == false){
            buildDB.insertGPS();
            gpsRead = true;
        }
    }
    
    public static boolean cellRead(){
        return cellRead;
    }
    
    public static void readCell(){
        if(cellRead == false){
            buildDB.insertBaseStation();
            cellRead = true;
        }
     }
    
    public static boolean battRead(){
        return battRead;
    }
    
    public static void readBatt(){
        if(battRead == false){
            buildDB.insertBattery();
            battRead = true;
        }
    }
    
    public void initDataBase(){
        
        buildDB.insertWIFI();
        DataBase.calcExactLocations();
//        buildDB.insertBattery();
//        buildDB.insertBaseStation();
//        buildDB.insertGPS();
    }
    
    public static ArrayList<AccessPoint> getAPs(){
        
        ArrayList<AccessPoint> aps = new ArrayList();
        
        for(Map.Entry<String, AccessPoint> entry : APs.entrySet()){
            aps.add(entry.getValue());
        }
        
        return aps;
    }
    
    public static User getUser(String user){
        
        return users.get(user);
    }
    
    public static AccessPoint getAP(String ap){
        
        return APs.get(ap);
    }
    
    
    ////////////////////////////////////////////////////////////////////PARSE///////////////////////////////////////////////////////////////////?
    
    
    public static void addApRecordUser(WifiRec record){
        
        User currUser = users.get(record.getId());
        
        if( currUser == null){
            User newUser = new User(record.getId());
            newUser.addApRecord(record);
            users.put(record.getId(),newUser);
        }
        else{
            currUser.addApRecord(record);
        }
    }
    
    public static void addApRecord(WifiRec record){
        
        AccessPoint currPoint = APs.get(record.getBSSID());
        
        if( currPoint == null){
            AccessPoint newPoint = new AccessPoint(record.getBSSID(),
                    record.getSSID(),record.getLevel(),
                    record.getFrequency(),null,null);
            newPoint.addRecord(record);
            APs.put(record.getBSSID(),newPoint);
        }
        else{
            currPoint.addRecord(record);
        }
    }
    
    
    public static void addBattRecordUser(BattRec record){

        
        User currUser = users.get(record.getId());
        
        if( currUser == null){
            User newUser = new User(record.getId());
            newUser.addBattRecord(record);
            users.put(record.getId(),newUser);
        }
        else{
            currUser.addBattRecord(record);
        }
        battRecs.put(record.getId(),record);
    }
    
    public static void addCellRecordUser(CellRec record){
        
        User currUser = users.get(record.getId());
        
        if( currUser == null){
            User newUser = new User(record.getId());
            newUser.addCellRecord(record);
            users.put(record.getId(),newUser);
        }
        else{
            currUser.addCellRecord(record);
        }
        cellRecs.put(record.getId(),record);
    }
    
    public static void addGPSRecordUser(GPSRec record){
        
        User currUser = users.get(record.getId());
        
        if( currUser == null){
            User newUser = new User(record.getId());
            newUser.addGPSRecord(record);
            users.put(record.getId(),newUser);
        }
        else{
            currUser.addGPSRecord(record);
        }
        gpsRecs.put(record.getId(), record);
    }

    ////////////////////////////////////////////////////////////////////PARSE///////////////////////////////////////////////////////////////////?
    
    public static void calcExactLocations(){
        
//        System.out.println("Found "+APs.size() + " different APs");
//        System.out.println("Processing...");
        int num = 0;
        for(Map.Entry<String, AccessPoint> entry : APs.entrySet()){
            entry.getValue().calcExactLocation();
            num++;
//            if(num % 1000 == 0)
//                System.out.println(num+" APs processed");
        }
    }
    
    public static ArrayList<User> getAllUsers(){
        
        ArrayList<User> allUsers = new ArrayList<User>();
        
        Iterator it = users.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            allUsers.add((User)pair.getValue());
        }
        
        return allUsers;
    }
    
    public static ArrayList<BattRec> getAllBattery(){
        
        if(!DataBase.battRead()){
            DataBase.readBatt();
        }
        
        ArrayList<BattRec> battRecords = new ArrayList<BattRec>();
        
        Iterator it = battRecs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            battRecords.add((BattRec)pair.getValue());
        }
        return battRecords;
    }
    
    public static ArrayList<CellRec> getAllCell(){
        
        if(!DataBase.cellRead()){
            DataBase.readCell();
        }
        
        ArrayList<CellRec> cellRecords = new ArrayList<CellRec>();
        
        Iterator it = cellRecs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            cellRecords.add((CellRec)pair.getValue());
        }
        
        return cellRecords;
    }
    
    public static ArrayList<StayPoint> calcAllStaypoints(){
        
        if(!DataBase.gpsRead()){
            DataBase.readGPS();
        }
        
        ArrayList<StayPoint> stayPoints = new ArrayList<StayPoint>();
        ArrayList<User> users = DataBase.getAllUsers();
        if (users != null) {
            Collections.sort(users, new CustomComparator());
        }
        for (User u : users){
        
            //            User user1 = DataBase.getUser("user56");
            ArrayList<StayPoint> stPoints = u.calcStayPoints(50000000.0,0.0,500000.0,0.0);
            stayPoints.addAll(stPoints);
        }
        return stayPoints;
    }
    
    public static int[] getLowBattery(){
        
        if(!DataBase.battRead()){
            DataBase.readBatt();
        }
        
        int [] battery = new int[24];
        
        int hour = 0;
        
        for(BattRec rec : DataBase.getAllBattery()){

//            if(rec.getLevel() >= 15){
////                System.out.println("Level was "+rec.getLevel());
//            }
                
            Date date = rec.getTimestamp();
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);   // assigns calendar to given date 
            int tempH = calendar.get(Calendar.HOUR_OF_DAY);
            battery[tempH]++;
        }
        System.out.println("Max was "+hour);
        return battery;
    }
    
    public static class NetworkInfo{
        
        public String carrier;
        public int number;
        
        public HashMap<String,String> users;
        
        public NetworkInfo(String carrier){
            this.carrier = carrier;
            number = 0;
            users = new HashMap<String,String>();
        }
    }
    
    public static ArrayList<NetworkInfo> getNetworkStats(){
        
        if(!DataBase.cellRead()){
            DataBase.readCell();
        }
        
        HashMap<String,NetworkInfo> info= new HashMap<String,NetworkInfo>();

        info.put("Cosmote", new NetworkInfo("Cosmote"));
        info.put("Vodafone", new NetworkInfo("Vodafone"));
        info.put("Wind", new NetworkInfo("Wind"));
        info.put("Orange", new NetworkInfo("Orange"));
        info.put("Sunrise", new NetworkInfo("Sunrise"));
        
//        HashMap<String,String> loggedUsers = new HashMap<String,String>();
        
        for( CellRec c : DataBase.getAllCell()){
            
//            if(loggedUsers.get(c.getId())!=null)
//                continue;
            
//            loggedUsers.put(c.getId(),c.getId());
            
            //Cosmote
            Pattern C = Pattern.compile("co", Pattern.CASE_INSENSITIVE);
            Matcher Cm = C.matcher(c.getOperator());
            if(Cm.find()){
                
                NetworkInfo inf = info.get("Cosmote");
                if(inf.users.get(c.getId())==null){
                    inf.number++;
                    inf.users.put(c.getId(),c.getId());
                }
                continue;
            }
            
            //Vodafone
            Pattern V = Pattern.compile("vod", Pattern.CASE_INSENSITIVE);
            Matcher Vm = V.matcher(c.getOperator());
            if(Vm.find()){
                
                NetworkInfo inf = info.get("Vodafone");
                if(inf.users.get(c.getId())==null){
                    inf.number++;
                    inf.users.put(c.getId(),c.getId());
                }
                continue;
            }
            
            //CU
            Pattern Cx = Pattern.compile("CU", Pattern.CASE_INSENSITIVE);
            Matcher Cxm = Cx.matcher(c.getOperator());
            if(Cxm.find()){
                
                NetworkInfo inf = info.get("Vodafone");
                if(inf.users.get(c.getId())==null){
                    inf.number++;
                    inf.users.put(c.getId(),c.getId());
                }
                continue;
            }
            
            //Wind
            Pattern W = Pattern.compile("wi", Pattern.CASE_INSENSITIVE);
            Matcher Wm = W.matcher(c.getOperator());
            if(Wm.find()){
                
                NetworkInfo inf = info.get("Wind");
                if(inf.users.get(c.getId())==null){
                    inf.number++;
                    inf.users.put(c.getId(),c.getId());
                }
                continue;
            }
            
            //Sunrise
            Pattern S = Pattern.compile("sun", Pattern.CASE_INSENSITIVE);
            Matcher Sm = S.matcher(c.getOperator());
            if(Sm.find()){
                
                NetworkInfo inf = info.get("Sunrise");
                if(inf.users.get(c.getId())==null){
                    inf.number++;
                    inf.users.put(c.getId(),c.getId());
                }
                continue;
            }
            
            //Orange
            Pattern O = Pattern.compile("Orange F", Pattern.CASE_INSENSITIVE);
            Matcher Om = O.matcher(c.getOperator());
            if(Om.find()){
                
                NetworkInfo inf = info.get("Orange");
                if(inf.users.get(c.getId())==null){
                    inf.number++;
                    inf.users.put(c.getId(),c.getId());
                }
                continue;
            }
        }
        ArrayList<NetworkInfo> info2 = new ArrayList<NetworkInfo>();
        
        Iterator it = info.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            info2.add((NetworkInfo)pair.getValue());
        }
        
        return info2;
    }
    
}
