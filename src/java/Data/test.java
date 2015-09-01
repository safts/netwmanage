/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;

/**
 *
 * @author sergios
 */
public class test {
    
    public static void main(String [] args) throws ParseException{
     
        Date date1 = new Date();
        Date date2 = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	String dateInString = "31-08-1982 10:20:56";
	Date date = sdf.parse(dateInString);
	
        
        if(date.getTime() < date2.getTime())
            System.out.println("Right!");
        else
            System.out.println(date1.toString()+" "+date2.toString());
        if(date2.getTime() > date.getTime())
            System.out.println("Right!");

    }
}
