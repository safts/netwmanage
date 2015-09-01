/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import DataBase.DataBase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergios
 */
public class User {
    
    private String id;
    
    private ArrayList<WifiRec> wifiRecs;
    private ArrayList<BattRec> battRecs;
    private ArrayList<CellRec> cellRecs;
    private ArrayList<GPSRec> gpsRecs;
    
    public User(String id){
        
        this.id = id;
        this.wifiRecs = new ArrayList<WifiRec>();
        this.battRecs = new ArrayList<BattRec>();
        this.cellRecs = new ArrayList<CellRec>();
        this.gpsRecs = new ArrayList<GPSRec>();

    }

    public void addApRecord(WifiRec record){
        wifiRecs.add(record);
    }
    
    public void addBattRecord(BattRec record){
        battRecs.add(record);
    }
    
    public void addCellRecord(CellRec record){
        cellRecs.add(record);
    }
    
    public void addGPSRecord(GPSRec record){
        gpsRecs.add(record);
    }
    
    public String getId() {
        return id;
    }

    public void setId(String Id) {
        this.id = id;
    }

    public ArrayList<WifiRec> getApRecordsUser(Date start,Date end){
        
        ArrayList<WifiRec> records = new ArrayList<WifiRec>();
        
        for(WifiRec record : wifiRecs){

            Date date;
            date = record.getTimestamp();
            if(date.getTime() >= start.getTime() && 
                date.getTime() <= end.getTime()){
                if(!records.contains(record))
                    records.add(record);
            }
                
        }
        return records;
    }
    
    public ArrayList<BattRec> getBattRecordsUser(Date start,Date end){
        
        if(!DataBase.battRead()){
            DataBase.readBatt();
        }
        
        ArrayList<BattRec> records = new ArrayList<BattRec>();
        
        if(start == null && end == null)
            return battRecs;
        
        for(BattRec record : battRecs){
            
            Date date = record.getTimestamp();
            if(date.getTime() >= start.getTime() && 
                date.getTime() <= end.getTime())
                records.add(record);
        }
        
        return records;
    }
    
    public ArrayList<CellRec> getCellRecordsUser(Date start,Date end){
        
        if(!DataBase.cellRead()){
            DataBase.readCell();
        }
        
        ArrayList<CellRec> records = new ArrayList<CellRec>();
        
        if(start == null && end == null)
            return cellRecs;
        
        for(CellRec record : cellRecs){
            
            Date date;
            date = record.getTimestamp();
            if(date.getTime() >= start.getTime() && 
                date.getTime() <= end.getTime())
            records.add(record);
        }
        
        return records;
    }
    
    public ArrayList<GPSRec> getGPSRecordsUser(Date start,Date end){
        
        if(!DataBase.gpsRead()){
            DataBase.readGPS();
        }
        
        ArrayList<GPSRec> records = new ArrayList<GPSRec>();
        
        for(GPSRec record : gpsRecs){
            Date date=record.getTimestamp();
            if(date.getTime() >= start.getTime() && 
                date.getTime() <= end.getTime())
            records.add(record);
        }
        
        return records;
    }
    
    @Override
    public String toString(){
        return id;
    }
    
    public ArrayList<StayPoint> calcStayPoints(Double tMax,Double tMin,
            Double dMax,Double dMin){
        
        if(!DataBase.gpsRead()){
            DataBase.readGPS();
        }
        
        ArrayList<StayPoint> stayPoints = new ArrayList<StayPoint>();
        
        // Cases for small list (size <2)
        
        for(int i=0;i<gpsRecs.size();i++){
            int j=i+1;
            for(;j<gpsRecs.size();j++){
                if(Utilities.timeDiff(gpsRecs.get(j),gpsRecs.get(j-1)) > tMax){
                   if(Utilities.timeDiff(gpsRecs.get(i),gpsRecs.get(j-1)) > tMax){
                       
                        stayPoints.add(EstimateStayPoint(
                               new ArrayList(gpsRecs.subList(i, j-1))));
                   }
                   i=j;
                   break;
                }
                else if(Utilities.distDiff(gpsRecs.get(i), gpsRecs.get(j))>dMax){
                    if(Utilities.timeDiff(gpsRecs.get(i),gpsRecs.get(j-1)) > tMin){
                       
                        stayPoints.add(EstimateStayPoint(
                               new ArrayList(gpsRecs.subList(i, j-1))));
                        i=j;
                        break;
                    }
                    i++;
                    break;
                }
                else if(j == gpsRecs.size()-1){
                    if(Utilities.timeDiff(gpsRecs.get(i),gpsRecs.get(j)) > tMin){
                       
                        stayPoints.add(EstimateStayPoint(
                                new ArrayList(gpsRecs.subList(i, j))));
                        i=j;
                        break;
                    }
                }
            }
        }
        System.out.println("Found "+stayPoints.size());
        return stayPoints;
    }
    
    public ArrayList<AccessPoint> getClosestAPs(ArrayList<GPSRec> coordinates){
        
        if(!DataBase.gpsRead()){
            DataBase.readGPS();
        }
        
        ArrayList<AccessPoint> APs = DataBase.getAPs();
        
        ArrayList<AccessPoint> closestAPs = new ArrayList<AccessPoint>();
        
        for(GPSRec record: coordinates){
            
            AccessPoint closest = null;
            Double dist = Double.POSITIVE_INFINITY;
            for(AccessPoint ap : APs){
                if(closest == null){
                    closest = ap;
                    dist = Utilities.distDiff(record, ap);
                }
                else{
                    Double tempDist = Utilities.distDiff(record, ap);
                    if(tempDist < dist){
                        dist = tempDist;
                        closest = ap;
                    }
                }
            }
            closestAPs.add(closest);
        }
        
        return closestAPs;
    }
    
    
    public StayPoint EstimateStayPoint(ArrayList<GPSRec> list){
        
        Double centrX = 0.0;
        Double centrY = 0.0;
        
        for(GPSRec rec : list){
            
            centrX+= Double.parseDouble(rec.getLatitude());
            centrY+= Double.parseDouble(rec.getLongitude());
        }
        
        centrX /= list.size();
        centrY /= list.size();
        
        
        return new StayPoint(centrX.toString(),centrY.toString(),
                list.get(0).getTimestamp(),
                list.get(list.size()-1).getTimestamp());
    }
}

