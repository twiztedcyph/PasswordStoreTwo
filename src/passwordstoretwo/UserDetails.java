
package passwordstoretwo;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Cypher
 */
public class UserDetails
{
    private String username;
    private String password;
    private String salt;
    private DbConnect dbConnect;
    private MakeHash makeHash;
    
    public UserDetails()
    {
        username = null;
        password = null;
        salt = null;
    }
    
    public UserDetails(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        this.username = username;
        this.password = getHashAndSalt(password)[1];
        this.salt = getHashAndSalt(password)[0];
    }
    
    public void persistData() throws ClassNotFoundException, SQLException
    {
        String sql = "insert into user_details values(default, ?, ?, ?)";
        dbConnect = new DbConnect();
        Connection myCon = dbConnect.getCon();
        PreparedStatement ps = myCon.prepareStatement(sql);
        ps.setString(1, this.username);
        ps.setString(2, this.password);
        ps.setString(3, this.salt);
        dbConnect.runUpdate(ps);
    }
    
    private String[] getHashAndSalt(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        makeHash = new MakeHash();
        return makeHash.genHash(password);
    }

    //<editor-fold defaultstate="collapsed" desc="Get and set methods... Boring">
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getSalt()
    {
        return salt;
    }
    
    public void setSalt(String salt)
    {
        this.salt = salt;
    }
    //</editor-fold>
}
