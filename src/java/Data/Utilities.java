/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import DataBase.DataBase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

/**
 *
 * @author sergios
 */
public class Utilities {
    
    
    public static final Double R = 6372.8; // In kilometers
    
    public static Double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
    
    public static Integer timeDiff(GPSRec rec1,GPSRec rec2){
        
        Double t1 = (double)rec1.getTimestamp().getTime();
        Double t2 = (double)rec2.getTimestamp().getTime();
        
        Double  timeDiff = Math.abs(t2-t1);
//        Double timeDiff = Math.abs(Double.longBitsToDouble(
//                 - rec2.getTimestamp().getTime()));
        return (timeDiff.intValue());
    }
    
    public static Double distDiff(GPSRec rec1,GPSRec rec2){
        
        return haversine(Double.parseDouble(rec1.getLatitude()),
                Double.parseDouble(rec1.getLongitude()),
                Double.parseDouble(rec2.getLatitude()),
                Double.parseDouble(rec2.getLongitude()));
    }
    
    public static Double distDiff(GPSRec rec1,AccessPoint rec2){
        
        return haversine(Double.parseDouble(rec1.getLatitude()),
                Double.parseDouble(rec1.getLongitude()),
                Double.parseDouble(rec2.getLatitude()),
                Double.parseDouble(rec2.getLongitude()));
    }
    
    public static List<Cluster<DoublePoint>> DBSCANClusterer(
            ArrayList<StayPoint> stayPoints,Double eParam,Integer minClust){

        DBSCANClusterer dbscan = new DBSCANClusterer(eParam, minClust);
        
        ArrayList<DoublePoint> input = new ArrayList<DoublePoint>();
        
        for(StayPoint st: stayPoints){
            double coord [] = new double[2];
            coord[0] = Double.parseDouble(st.getLatitude());
            coord[1] = Double.parseDouble(st.getLongitude());
            input.add(new DoublePoint(coord));
        }

        List<Cluster<DoublePoint>> cluster = dbscan.cluster(input); 
        
        return cluster;
    }
    
    public static DoublePoint getCentroid(Cluster<DoublePoint> dp){
        
        double [] coords = new double[2];
        
        List<DoublePoint> clPoints = dp.getPoints();
        
        for(DoublePoint p : clPoints){
            coords[0]+=p.getPoint()[0];
            coords[1]+=p.getPoint()[1];
        }
        
        coords[0]/=clPoints.size();
        coords[1]/=clPoints.size();

        return new DoublePoint(coords);
    }
    
    
}
