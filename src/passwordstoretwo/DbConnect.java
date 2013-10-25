
package passwordstoretwo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cypher
 */
public class DbConnect
{
    private final String USERNAME = "cypher";
    private final String PASSWORD = "cypher";
    private final String DATABASE = "passwordstore";
    private final String PATH = "jdbc:postgresql://localhost:";
    private final String CLASS = "org.postgresql.Driver";
    private final int PORT = 5432;
    
    private Connection myCon = null;
    
    public DbConnect()
    {
        Connection testCon = null;
        System.out.println("Checking for database.");
        try
        {
            testCon = getCon();
        } catch (ClassNotFoundException cnfe)
        {
            System.out.println("Class not found error:");
            System.out.println(cnfe);
        } catch (SQLException sqle)
        {
            if(sqle.getSQLState().equals("3D000"))
            {
                System.out.println("Database does not exist.");
                System.out.println("Please wait while one is created.");
                try
                {
                    makeDatabase();
                    testCon = getCon();
                } catch (SQLException | ClassNotFoundException sqlex)
                {
                    System.out.println("Failed to create database:");
                    System.out.println(sqlex);
                }
            }else
            {
                System.out.println("SQL error:");
                System.out.println(sqle);
            }
        }
        try
        {
            if(testCon.isValid(5))
            {
                System.out.println("Database ready.");
                testCon.close();
            }
        } catch (SQLException sqle)
        {
            System.out.println("SQL error:");
                System.out.println(sqle);
        }
    }
    
    public final Connection getCon() throws ClassNotFoundException, SQLException
    {
        Class.forName(CLASS);
        return DriverManager.getConnection(PATH + PORT + "/" + DATABASE
                , USERNAME, PASSWORD);
    }
    
    public ResultSet runQuery(PreparedStatement query) throws 
            ClassNotFoundException, SQLException
    {
        ResultSet myRs;
        myCon = query.getConnection();
        myRs = query.executeQuery();
        query.close();
        myCon.close();
        return myRs;
    }
    
    public int runUpdate(PreparedStatement update) throws SQLException
    {
        int result;
        myCon = update.getConnection();
        result = update.executeUpdate();
        myCon.close();
        return result;
    }
    
    private void makeDatabase() throws SQLException, ClassNotFoundException
    {
        PreparedStatement makeUserDetailsTable, makeLoginDetailsTable, makeDb;
        Connection makeDbCon;
        String userTblSql = "CREATE TABLE user_details(id SERIAL NOT NULL"
                + ", username VARCHAR(200) NOT NULL UNIQUE"
                + ", password VARCHAR(128) NOT NULL"
                + ", pwsalt VARCHAR(30)"
                + ", PRIMARY KEY(id))";
        String storeTblSql = "CREATE TABLE store_details(id SERIAL NOT NULL"
                + ", username VARCHAR(200) NOT NUll REFERENCES user_details(username)"
                + ", siteusername VARCHAR(200) NOT NULL"
                + ", sitepassword VARCHAR(200) NOT NULL"
                + ", sitenotes VARCHAR(500)"
                + ", PRIMARY KEY(id))";
        
        Class.forName(CLASS);
        makeDbCon = DriverManager.getConnection(PATH + PORT + "/" + "template1"
                , USERNAME, PASSWORD);
        makeDb = makeDbCon.prepareStatement("CREATE DATABASE " + DATABASE);
        makeDb.executeUpdate();
        makeDbCon.close();
        makeDbCon = DriverManager.getConnection(PATH + PORT + "/" 
                + "passwordstore", USERNAME, PASSWORD);
        System.out.println("Creating tables.");
        makeUserDetailsTable = makeDbCon.prepareStatement(userTblSql);
        makeUserDetailsTable.executeUpdate();
        makeLoginDetailsTable = makeDbCon.prepareStatement(storeTblSql);
        makeLoginDetailsTable.executeUpdate();
        makeDbCon.close();
    }
}
