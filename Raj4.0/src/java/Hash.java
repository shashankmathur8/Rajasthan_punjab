

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hash functions utility class.
 *
 * @author www.codejava.net
 */
public class Hash {
    private Hash() {
    }

    public static String generateMD5(String message){
        return hashString(message, "MD5");
    }

    public static String generateSHA1(String message){
        return hashString(message, "SHA-1");
    }

    public static String generateSHA256(String message){
        return hashString(message, "SHA-256");
    }

    public static String generateMD5(File file){
        return hashFile(file, "MD5");
    }

    public static String generateSHA1(File file){
        return hashFile(file, "SHA-1");
    }

    public static String generateSHA256(File file){
        return hashFile(file, "SHA-256");
    }

    private static String hashString(String message, String algorithm)
             {

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            System.out.print("Could not generate hash from String");
        }
        return "";
    }

    private static String hashFile(File file, String algorithm) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            byte[] bytesBuffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
                digest.update(bytesBuffer, 0, bytesRead);
            }

            byte[] hashedBytes = digest.digest();

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | IOException ex) {
        }
    return "";
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (byte arrayByte : arrayBytes) {
            stringBuffer.append(Integer.toString((arrayByte & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}