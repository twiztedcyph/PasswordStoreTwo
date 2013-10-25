
package passwordstoretwo;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Cypher
 */
public class MakeHash
{
    private String specInstance;
    private String secRandInstance;
    private int iterations;
    
    public MakeHash()
    {
        specInstance = "PBKDF2WithHmacSHA1";
        secRandInstance = "SHA1PRNG";
        iterations = 1000;
    }
    
    public final String genHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String result;
        
        char[] passChars = password.toCharArray();
        byte[] salt = getSalt().getBytes(); //Get salt will go here...
        
        PBEKeySpec pbeSpec = new PBEKeySpec(passChars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(specInstance);
        
        byte[] passHash = skf.generateSecret(pbeSpec).getEncoded();
        
        result = toHex(passHash);
        
        return toHex(salt) + "\n" +result;
    }
    
    private String toHex(byte[] byteIn)
    {
        BigInteger bigInt = new BigInteger(1, byteIn);
        String hex = bigInt.toString(16);
        int padding = (byteIn.length * 2) - hex.length();
        if(padding > 0)
        {
            return String.format("%0"  +padding + "d", 0) + hex;
        }else{
            return hex;
        }
    }
    
    private String getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom secRand = SecureRandom.getInstance(secRandInstance);
        byte[] salt = new byte[16];
        secRand.nextBytes(salt);
        return salt.toString();
    }
}
