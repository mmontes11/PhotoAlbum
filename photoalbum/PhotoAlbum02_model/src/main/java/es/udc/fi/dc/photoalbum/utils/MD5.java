package es.udc.fi.dc.photoalbum.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * For getting MD5 hash
 * 
 * @author usuario
 * @version $Revision: 1.0 $
 */
public final class MD5 {

    /**
     * Constructor for MD5.
     */
    private MD5() {
    }

    private static final int HEX_TO_STRING = 0xFF;

    /**
     * @param str
     *            string, MD5 of which returned
     * 
     * @return md5 hash
     */
    public static String getHash(String str) {
        MessageDigest md5;
        StringBuilder hexString = new StringBuilder();
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.reset();
            md5.update(str.getBytes());
            byte[] messageDigest = md5.digest();
            for (byte aMessageDigest : messageDigest) {
                hexString.append(Integer.toHexString(HEX_TO_STRING
                        & aMessageDigest));
            }
        } catch (NoSuchAlgorithmException e) {
            return e.toString();
        }
        return hexString.toString();
    }
}
