/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergios
 */
public class WifiRec {
    
    private String id;
    private String SSID;
    private String BSSID;
    private int level;

    
    private String latitude;
    private String longitude;
    private Date timestamp;
    private String frequency;

    public WifiRec(String id,String SSID,String BSSID, int level,String latitude,
            String longitude,String timestamp,String frequency){
        
        this.id = id;
        this.BSSID = new String(BSSID);
        this.SSID = new String(SSID);
        this.level = level;
        this.latitude = new String(latitude);
        this.longitude = new String(longitude);
        
        SimpleDateFormat sdf = new SimpleDateFormat("y-M-d hh:mm:ss");
        Date temp=null;
        
        try {
            temp = sdf.parse(timestamp);
        } catch (ParseException ex) {
            Logger.getLogger(WifiRec.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.timestamp = temp;
        this.frequency = new String(frequency);
        
    }

    
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
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
    
    @Override
    public boolean equals(Object rec){
        
        WifiRec recW = (WifiRec) rec;
        if(recW.BSSID.equals(BSSID))
            return true;
        return false;
    }
}
