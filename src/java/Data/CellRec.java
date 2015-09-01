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
public class CellRec {
    
    private String id;
    private String mmc;
    private String mnc;
    private String cid;
    private String lac;
    private String latitude;
    private String longitude;
    private String operator;
    private Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public CellRec(String id, String operator,
            String mmc,String mnc,String cid, String lac,
            String latitude,String longitude,String timestamp){
        
        this.id = id;
        this.operator = operator;
        this.latitude = latitude;
        this.longitude = longitude;
        
        this.mmc = mmc;
        this.mnc = mnc;
        this.cid = cid;
        this.lac = lac;
        
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMmc() {
        return mmc;
    }

    public void setMmc(String mmc) {
        this.mmc = mmc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }
    
    
    
}
