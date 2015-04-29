package com.cloudstaff.cstm.utils;

import android.content.Context;
import android.provider.Settings;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by arneln on 3/30/15.
 */
public class AndroidCodes {
    Context mContext;

    public AndroidCodes(Context context) {
        mContext = context;
    }

    static public String md5(String word) {
        String created_hash = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(word.getBytes());
            BigInteger hash = new BigInteger(1, md5.digest());
            created_hash = hash.toString(16);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception : " + e.getMessage());
        }
        return created_hash;
    }

    private String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public String SHA1(String text) {
        byte[] sha1hash = new byte[0];
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            sha1hash = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertToHex(sha1hash);
    }

    public String getDeviceID() {
        String deviceID = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceID;
    }

    public String getDeviceName() {
        return android.os.Build.MODEL;
    }

}
