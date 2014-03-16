package com.example.servertest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件路径加密
 * 
 * @author guozhuoxing
 *
 */
public class MD5 {
	 
    /**
     * 加密
     * @param content
     * @return getHashString(digest);
     */
    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getHashString(digest);
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 路径转成String路径
     * @param digest
     * @return  path
     */
    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }
}
