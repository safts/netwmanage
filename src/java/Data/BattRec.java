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
public class BattRec {
    
    private String id;
    private Integer level;
    private Date timestamp;
    
    public BattRec(String id,String level,String timestamp){
    
        this.id = id;
        this.level = Integer.parseInt(level);
        
        Date start = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            start = sdf.parse(timestamp);
        } catch (ParseException ex) {
            Logger.getLogger(BattRec.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        this.timestamp = start;
    }
    
     public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
