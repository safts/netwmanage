/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import Data.BattRec;
import Data.CellRec;
import Data.GPSRec;
import Data.WifiRec;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergios
 */
public class buildDB {
    
    private static Connection connection;
    
    public static void insertBaseStation(){
        
        
        try {
            String current = new java.io.File( "." ).getCanonicalPath();
            System.out.println("Current dir:"+current);
            
            FileInputStream fis = new FileInputStream("../input/base_station.csv");
            String line = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            
            line = br.readLine();   // skip first line!
            
            while ((line = br.readLine()) != null) {
                
                Scanner read = new Scanner (line);
                read.useDelimiter("\t");
                String id, email, operator,
                        mmc, mnc, cid ,lac, latitude,longitude,
                        timestamp;

                id = read.next();
                email = read.next();
                operator = read.next();
                
                mmc = read.next();
                mnc = read.next();
                cid = read.next();
                lac = read.next();
                
                latitude = read.next();
                longitude = read.next();
                timestamp = read.next();
                
                CellRec record = new CellRec(email,operator,mmc,mnc,cid,
                        lac,latitude,longitude,timestamp);
                
                DataBase.addCellRecordUser(record);
                
                read.close();
            }
            
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(buildDB.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }catch (IOException ex) {
            Logger.getLogger(buildDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void insertBattery(){
        
        try {
            
            String current = new java.io.File( "." ).getCanonicalPath();
            System.out.println("Current dir:"+current);
            
            FileInputStream fis = new FileInputStream("../input/battery.csv");
            String line = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            
            line = br.readLine();   // skip first line!
            
            while ((line = br.readLine()) != null) {
                
                Scanner read = new Scanner (line);
                read.useDelimiter("\t");
                String id, level, plugged,email,
                        temperature, voltage, timestamp;

                id = read.next();
                email = read.next();
                level = read.next();
                plugged = read.next();
                temperature = read.next();
                voltage = read.next();
                
                timestamp = read.next();
                
                BattRec record = new BattRec(email,level,timestamp);
                
                DataBase.addBattRecordUser(record);
                
                read.close();
            }
            
            br.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(buildDB.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }catch (IOException ex) {
            Logger.getLogger(buildDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void insertGPS(){
        
        try {
            
            String current = new java.io.File( "." ).getCanonicalPath();
            System.out.println("Current dir:"+current);
            
            FileInputStream fis = new FileInputStream("../input/gps.csv");
            String line = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            
            line = br.readLine();   // skip first line!
            
            while ((line = br.readLine()) != null) {
                
                Scanner read = new Scanner (line);
                read.useDelimiter("\t");
                String id, email,
                        latitude,longitude, timestamp;

                id = read.next();
                email = read.next();
                
                latitude = read.next();
                longitude = read.next();
                
                timestamp = read.next();
                
                GPSRec record = new GPSRec(email,latitude,longitude,timestamp);
                
                DataBase.addGPSRecordUser(record);
                
                read.close();
            }
            
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(buildDB.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }catch (IOException ex) {
            Logger.getLogger(buildDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void insertWIFI(){
     
        /*
        `id` int(11) NOT NULL,
        `email` varchar(64) NOT NULL,
        `ssid` varchar(128) NOT NULL,
        `bssid` varchar(128) NOT NULL,
        `level` int(11) NOT NULL,
        `frequency` int(11) NOT NULL,
        `latitude` varchar(64) NOT NULL,
        `longitude` varchar(64) NOT NULL,
        `timestamp` DATETIME NOT NULL,
        */
        
        try {
            
//            connection = ConnectionInit.Datasource.getConnection();
            String current = new java.io.File( "." ).getCanonicalPath();
            System.out.println("Current dir:"+current);
            
            FileInputStream fis = new FileInputStream("../input/wifi.csv");
            String line = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            
            line = br.readLine();   // skip first line!
            
            while ((line = br.readLine()) != null) {
                
                Scanner read = new Scanner (line);
                read.useDelimiter("\t");
                String id, email,ssid,bssid,level,
                        frequency,latitude,longitude, timestamp;

                id = read.next();
                email = read.next();
                ssid = read.next();
                bssid = read.next();
                level = read.next();
                frequency = read.next();
                latitude = read.next();
                longitude = read.next();
                
                timestamp = read.next();
                               
                WifiRec record = new WifiRec(email, ssid,
                          bssid,Integer.parseInt(level)
                        ,latitude,longitude,timestamp,frequency);
                
                DataBase.addApRecordUser(record);
                DataBase.addApRecord(record);
                
                read.close();
                
            }
//            DataBase.calcExactLocations();
            
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(buildDB.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }catch (IOException ex) {
            Logger.getLogger(buildDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
