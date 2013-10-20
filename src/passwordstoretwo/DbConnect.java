
package passwordstoretwo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Cypher
 */
public class DbConnect
{
    private final String USERNAME = "cypher";
    private final String PASSWORD = "cypher";
    private final String DATABASE = "passwordstore";
    private final int PORT = 5432;
    
    private Connection myCon = null;
    
    public Connection getCon() throws ClassNotFoundException, SQLException
    {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection("jdbc:postgresql:"+DATABASE, USERNAME, PASSWORD);
    }
    
    public ResultSet runQuery(PreparedStatement query) throws ClassNotFoundException, SQLException
    {
        ResultSet myRs;
        myCon = query.getConnection();
        myRs = query.executeQuery();
        myCon.close();
        return myRs;
    }
}
