package Utils;

import org.apache.commons.codec.binary.Hex;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PBKDF2Util {

    public static String generateDerivedKey(String password, String salt, Integer iterations) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterations, 128);
        SecretKeyFactory pbkdf2;
        String derivedPass = null;
        try {
            pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            SecretKey secretKey = pbkdf2.generateSecret(spec);
            derivedPass = Hex.encodeHexString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return derivedPass;
    }

    public static String getSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Hex.encodeHexString(salt);
    }
}
