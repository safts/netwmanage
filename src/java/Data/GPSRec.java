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
public class GPSRec {
    
    private String id;
    private String latitude;
    private String longitude;
    private Date timestamp;
    
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public GPSRec(String id, String latitude,String longitude,String timestamp){
        
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        Date temp=null;
        
        try {
            temp = sdf.parse(timestamp);
        } catch (ParseException ex) {
            Logger.getLogger(WifiRec.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.timestamp = temp;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
