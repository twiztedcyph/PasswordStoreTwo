
package passwordstoretwo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cypher
 */
public class PasswordStoreTwo
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        DbConnect db = new DbConnect();
        ResultSet myRs = null;
        try
        {
            PreparedStatement query = db.getCon().prepareStatement(
                    "SELECT * FROM user_details WHERE username  = ?"
                    , ResultSet.TYPE_SCROLL_INSENSITIVE
                    , ResultSet.CONCUR_UPDATABLE);
            
            query.setString(1, "IanWeeks");
            myRs = db.runQuery(query);
        } catch (ClassNotFoundException | SQLException ex)
        {
            System.out.println("Exception: " + ex);
        }
    }
}
