
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
        try
        {
            PreparedStatement update = db.getCon().prepareStatement(
                    "INSERT INTO user_details(username, password) VALUES(?, ?)"
                    , ResultSet.TYPE_SCROLL_INSENSITIVE
                    , ResultSet.CONCUR_UPDATABLE);
            update.setString(1, "thenewguy");
            update.setString(2, "newpass");
            System.out.println(db.runUpdate(update));
            
        } catch (ClassNotFoundException CNFE)
        {
            System.out.println("Class exception: " + CNFE);
        } catch (SQLException SQLE)
        {
            if(SQLE.getSQLState().equals("23505"))
            {
                System.out.println("That username already exists.");
            }else
            {
                System.out.println("SQL exception: " + SQLE);
            }
        }
    }
}
