package assignment3;

// Made by Matt Ripia - 1385931

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database 
{
    public Connection conn = null;
    public Statement stmt = null;
    
    public Database(String user, String pass, String driver, String connectionString)
    {
        try 
        {
            Class.forName(driver);
            conn = DriverManager.getConnection(connectionString, user, pass);
            stmt = conn.createStatement();
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet queryDB(String query)
    {
        if(stmt != null)
        {
            try 
            {
                return stmt.executeQuery(query);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }
}


