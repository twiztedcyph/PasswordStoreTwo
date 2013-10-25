
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
    
    public final String[] genHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] result = new String[2];
        
        char[] passChars = password.toCharArray();
        byte[] salt = getSalt().getBytes(); //Get salt will go here...
        
        PBEKeySpec pbeSpec = new PBEKeySpec(passChars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(specInstance);
        
        byte[] passHash = skf.generateSecret(pbeSpec).getEncoded();
        
        result[0] = toHex(salt);
        result[1] = toHex(passHash);
        
        return result;
    }
    
    public final String[] genHash(String password, String preSalt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String result[] = new String[2];
        
        char[] passChars = password.toCharArray();
        byte[] salt = fromHex(preSalt); //Get salt will go here...
        
        PBEKeySpec pbeSpec = new PBEKeySpec(passChars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(specInstance);
        
        byte[] passHash = skf.generateSecret(pbeSpec).getEncoded();
        
        result[0] = toHex(salt);
        result[1] = toHex(passHash);
        
        return result;
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
    
    private byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
    
    private String getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom secRand = SecureRandom.getInstance(secRandInstance);
        byte[] salt = new byte[16];
        secRand.nextBytes(salt);
        return salt.toString();
    }
}
