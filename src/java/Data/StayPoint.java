/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author sergios
 */
public class StayPoint {
    
    private String latitude;
    private String longitude;
    private Date start;
    private Date end;
    
    public StayPoint(String latitude,String longitude,Date start,
            Date end){
        
        this.latitude = latitude;
        this.longitude = longitude;
        this.start = start;
        this.end = end;
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
    
    
}
