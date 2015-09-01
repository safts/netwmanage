/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

/**
 *
 * @author sergios
 */
public class ConnectionInit implements ServletContextListener {
    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

    public static DataSource Datasource;

    /**
     *
     * @param arg0
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
	System.out.println("Context Destroyed");
    }

    /**
     *	Initialises the Connection Pool (database connection)
     * @param arg0
     */
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
	try {
            
            DataBase db = new DataBase();
            db.initDataBase();
	    System.out.println("Context Created");

	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}
    }
}
