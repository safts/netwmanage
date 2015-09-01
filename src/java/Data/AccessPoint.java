/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.util.ArrayList;

/**
 *
 * @author sergios
 */



public class AccessPoint {
    
    private String BSSID;
    private String latitude;
    private String longitude;
    private String SSID;
    private int level;
    private String frequency;
    
    private ArrayList<WifiRec> records;
    
    public AccessPoint(String BSSID,String SSID,int level,String frequency,
            String latitude,String longitude){
        
        this.BSSID = BSSID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.SSID = SSID;
        this.level = level;
        this.records = new ArrayList<WifiRec>();
        this.frequency = frequency;
    }

    public void addRecord(WifiRec record){
        records.add(record);
    }
    
    public void calcExactLocation(){
        
        //TODO algorithm
        Double lat=0.0;
        Double lon=0.0;
        Double totalWeight=0.0;
        Double RSSI = 0.0;
        
        
        int i=0;
        
        for(WifiRec rec:records){
            
//            if(i<1){
//                System.out.println("Old lat ="+Double.parseDouble(rec.getLatitude()));
//                System.out.println("Old lon ="+Double.parseDouble(rec.getLongitude()));
//                i++;
//            }
            
            Double tempLat = (Double.parseDouble(rec.getLatitude()) 
                    *Math.PI )/ 180;
            Double tempLon = Double.parseDouble(rec.getLongitude()) 
                    *Math.PI / 180;
            Double tempW = Math.pow(10,rec.getLevel()/10) /1000;
            
            totalWeight +=tempW;
            
            lat += tempLat * tempW;
            lon += tempLon * tempW;
            RSSI+=rec.getLevel();
        }
        
        lat = lat / totalWeight;
        lat *= (180 / Math.PI);
        
        lon = lon /totalWeight;
        lon *= (180 / Math.PI);
        
        latitude = lat.toString();
        longitude = lon.toString();
        RSSI /=records.size();
        level = RSSI.intValue();
        
//        System.out.println("Recalculated lat ="+latitude);
//        System.out.println("Recalculated lon ="+longitude);
    }
    
    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    
    
}
